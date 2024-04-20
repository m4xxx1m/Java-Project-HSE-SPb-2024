package com.example.server.service;


import com.example.server.dto.ContentObjDto;
import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.model.RatedObject;
import com.example.server.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private RatedObjectService ratedObjectService;

    public Comment addComment(ContentObjDto commentRequest, int postId) {
        Comment comment = new Comment(commentRequest.getAuthorId(), commentRequest.getContent(), postId);
        postService.incrementCommentsCount(postId);
        return commentRepository.save(comment);
    }

    public void deleteComment(int id) {
        Comment comment = getCommentById(id);
        postService.decrementCommentsCount(comment.getPostId());
        ratedObjectService.deleteRatingsOfObject(id);
        savedObjectService.deleteSavedObjectForAllUsers(id);
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
            return commentRepository.findByPostId(postId);
        }
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

    public void incrementCommentRating(int commentId, int userId) {
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
    }

    public void decrementCommentRating(int commentId, int userId) {
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
    }

    public void editComment(Comment comment, ContentObjDto commentDto) {
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

}