package com.example.server.repository;

import com.example.server.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByAuthorId(int authorId);
    Optional<Post> findById(int id);
    List<Post> findByIdLessThan(int id, Pageable pageable);

}