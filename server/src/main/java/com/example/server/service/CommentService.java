package com.example.server.service;


import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.model.User;
import com.example.server.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByPostId(Post postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentsByAuthorId(User authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

}