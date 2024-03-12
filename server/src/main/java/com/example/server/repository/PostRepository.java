package com.example.server.repository;

import com.example.server.model.Post;
import com.example.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByAuthorId(User authorId);
    Optional<Post> findById(long id);

    void deleteById(long id);

}