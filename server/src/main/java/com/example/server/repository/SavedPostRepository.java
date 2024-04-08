package com.example.server.repository;

import com.example.server.model.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedPostRepository extends JpaRepository<SavedPost, Integer> {
    List<SavedPost> findByPostId(int postId);
}
