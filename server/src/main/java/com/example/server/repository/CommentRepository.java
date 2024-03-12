package com.example.server.repository;

import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostId(long postId);

    List<Comment> findByAuthorId(long authorId);

    Optional<Comment> findById(long id);

    void deleteById(long id);

}