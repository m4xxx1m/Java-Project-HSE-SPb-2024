package com.example.server.repository;

import com.example.server.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostId(int postId);

    List<Comment> findByAuthorId(int authorId);

    Optional<Comment> findById(int id);

    List<Comment> findByPostIdAndIdGreaterThan(int postId, int id, Pageable pageable);

    void deleteById(int id);

}