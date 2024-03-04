package com.example.server.repository;

import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostId(Post postId);

    List<Comment> findByAuthorId(User authorId);

}