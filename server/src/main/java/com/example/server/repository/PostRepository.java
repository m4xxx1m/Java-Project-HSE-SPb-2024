package com.example.server.repository;

import com.example.server.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByAuthorId(int authorId);

    Optional<Post> findById(int id);

    @Query(value = "SELECT * FROM content_obj WHERE content @@ :content", nativeQuery = true)
    List<Post> findByContentUsingTrigram(@Param("content") String content);
}