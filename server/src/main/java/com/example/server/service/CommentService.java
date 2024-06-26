package com.example.server.service;


import com.example.server.controller.NotificationController;
import com.example.server.dto.ContentObjDto;
import com.example.server.model.Comment;
import com.example.server.model.Notification;
import com.example.server.model.Post;
import com.example.server.model.RatedObject;
import com.example.server.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SavedObjectService savedObjectService;

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RatedObjectService ratedObjectService;

    @Autowired
    private NotificationController notificationController;

    public Comment addComment(ContentObjDto commentRequest, int postId) {
        Comment comment = new Comment(
                commentRequest.getAuthorId(),
                commentRequest.getContent(),
                postId,
                commentRequest.getReplyToCommentId()
        );
        commentRepository.save(comment);
        postService.changeCommentsCount(postId, 1);
        if (comment.getReplyToCommentId() != -1 &&
                comment.getAuthorId() != getCommentById(comment.getReplyToCommentId()).getAuthorId()) {
            int originalCommentId = comment.getReplyToCommentId();
            int originalCommentAuthorId = getCommentById(originalCommentId).getAuthorId();
            Notification notification = new Notification(postId, originalCommentId,
                    originalCommentAuthorId, comment.getId(), comment.getAuthorId());
            notificationService.saveNotification(notification);
            notificationController.notifyUserAboutReply(originalCommentAuthorId, notification);
        }
        return comment;
    }

    public void deleteComment(int id) {
        ratedObjectService.deleteRatingsOfObject(id);
        savedObjectService.deleteSavedObjectForAllUsers(id);
        Comment comment = getCommentById(id);
        if (comment != null) {
            postService.changeCommentsCount(comment.getPostId(), -1);
        }
        notificationService.deleteNotificationsByReplyCommentId(id);
        notificationService.deleteNotificationsByOriginalCommentId(id);
        commentRepository.deleteById(id);
    }

    public Comment getCommentById(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByPostId(int postId) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            return null;
        } else {
            var comments = commentRepository.findByPostId(postId);
            comments.sort(Comparator.comparing(Comment::getCreationTime));
            return comments;
        }
    }

    public List<Comment> getCommentsAfterId(int postId, int prevId, int size) {
        return commentRepository.findByPostIdAndIdGreaterThan(postId, prevId,
                PageRequest.of(0, size, Sort.by("id").ascending()));
    }

    public List<Comment> getCommentsByAuthorId(int authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    public List<Comment> getCommentsByCommentIds(List<Integer> commentIds) {
        List<Comment> comments = new ArrayList<>();
        for (Integer commentId : commentIds) {
            Comment comment = getCommentById(commentId);
            if (comment != null) {
                comments.add(comment);
            }
        }
        return comments;
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public int incrementCommentRating(int commentId, int userId) {
        Comment comment = getCommentById(commentId);
        Optional<RatedObject.Type> rating = ratedObjectService.getObjectRating(userId, commentId);
        if (rating.isEmpty()) {
            ratedObjectService.rateObject(userId, commentId, RatedObject.Type.LIKE);
            comment.incrementRating();
        } else if (rating.get() == RatedObject.Type.LIKE) {
            ratedObjectService.deleteObjectRating(userId, commentId);
            comment.decrementRating();
        } else {
            ratedObjectService.changeRating(userId, commentId);
            comment.incrementRating();
            comment.incrementRating();
        }
        commentRepository.save(comment);
        return comment.getRating();
    }

    public int decrementCommentRating(int commentId, int userId) {
        Comment comment = getCommentById(commentId);
        Optional<RatedObject.Type> rating = ratedObjectService.getObjectRating(userId, commentId);
        if (rating.isEmpty()) {
            ratedObjectService.rateObject(userId, commentId, RatedObject.Type.DISLIKE);
            comment.decrementRating();
        } else if (rating.get() == RatedObject.Type.DISLIKE) {
            ratedObjectService.deleteObjectRating(userId, commentId);
            comment.incrementRating();
        } else {
            ratedObjectService.changeRating(userId, commentId);
            comment.decrementRating();
            comment.decrementRating();
        }
        commentRepository.save(comment);
        return comment.getRating();
    }

    public void editComment(Comment comment, ContentObjDto commentDto) {
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

}