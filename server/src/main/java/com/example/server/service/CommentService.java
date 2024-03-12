package com.example.server.service;


import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.model.User;
import com.example.server.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    public Comment getCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByPostId(long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentsByAuthorId(long authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    List<Comment> getCommentsByCommentIds(List<Long> commentIds) {
        List<Comment> comments = new ArrayList<>();
        for (Long commentId : commentIds) {
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

    void incrementCommentRating(long id) {
        Comment comment = getCommentById(id);
        comment.incrementRating();
    }

    void decrementPostRating(long id) {
        Comment comment = getCommentById(id);
        comment.decrementRating();
    }



}