package com.example.server.repository;

import com.example.server.model.Post;
import com.example.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByAuthorId(User authorId);

}