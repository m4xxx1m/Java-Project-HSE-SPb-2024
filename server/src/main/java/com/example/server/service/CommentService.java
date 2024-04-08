package com.example.server.service;


import com.example.server.dto.ContentObjDto;
import com.example.server.dto.PostDto;
import com.example.server.model.Comment;
import com.example.server.model.ContentObj;
import com.example.server.model.Post;
import com.example.server.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    public Comment addComment(ContentObjDto commentRequest, int postId) {
        Comment comment = new Comment(commentRequest.getAuthorId(), commentRequest.getContent(), postId);
        return commentRepository.save(comment);
    }

    public void deleteComment(int id) {
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

    List<Comment> getCommentsByCommentIds(List<Integer> commentIds) {
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

    public void incrementCommentRating(int id) {
        Comment comment = getCommentById(id);
        comment.incrementRating();
        commentRepository.save(comment);
    }

    public void decrementCommentRating(int id) {
        Comment comment = getCommentById(id);
        comment.decrementRating();
        commentRepository.save(comment);
    }

    public void editComment(Comment comment, ContentObjDto commentDto) {
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

}