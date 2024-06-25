package com.example.server.repository;

import com.example.server.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByAuthorId(int authorId);

    Optional<Post> findById(int id);
    List<Post> findByIdLessThan(int id, Pageable pageable);

    // @Query(value = "SELECT * FROM content_obj WHERE word_similarity(content, :content) > 0.4", nativeQuery = true)
    // List<Post> findByContentUsingTrigram(@Param("content") String content);}

    @Query(value = "SELECT * FROM content_obj WHERE CAST(CAST(tags AS bit(63)) AS bigint) " +
            "& CAST(CAST(:tags AS bit(63)) AS bigint) > 0" +
            " AND dtype = 'Post'",
            nativeQuery = true)
    List<Post> findByTagsLike(@Param("tags") String tags, Pageable pageable);

    @Query(value = "SELECT * FROM content_obj WHERE id < :id " +
            "AND CAST(CAST(tags AS bit(63)) AS bigint) " +
            "& CAST(CAST(:tags AS bit(63)) AS bigint) > 0" +
            " AND dtype = 'Post'",
            nativeQuery = true)
    List<Post> findByIdLessThanAndTagsLike(@Param("id") int id, @Param("tags") String tags, Pageable pageable);

    @Query(value = "SELECT * FROM content_obj " +
            "WHERE (content @@ :content OR title @@ :content) " +
            "AND dtype = 'Post'",
            nativeQuery = true)
    List<Post> findByContentUsingTrigram(@Param("content") String content);
}
