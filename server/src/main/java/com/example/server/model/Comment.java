package com.example.server.model;


import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;
    @ManyToOne
    private final User authorId;
    @ManyToOne
    private final Post postId;
    @Temporal(TemporalType.TIMESTAMP)
    private final Date creationTime;
    @Column(length = 4096)
    private String content;
    private Integer commentRating;

    public Comment(User authorId, Post postId, String content) {
        this.authorId = authorId;
        this.postId = postId;
        this.content = content;
        this.creationTime = new Date();
        this.commentRating = 0;
    }

    Integer getCommentId() {
        return commentId;
    }

    User getAuthorId() {
        return authorId;
    }

    Post getPostId() {
        return postId;
    }

    Date getCreationTime() {
        return creationTime;
    }

    String getContent() {
        return content;
    }

    Integer getCommentRating() {
        return commentRating;
    }

    void setContent(String content) {
        this.content = content;
    }

    public void incrementRating() {
        this.commentRating++;
    }

    public void decrementRating() {
        this.commentRating--;
    }

}
