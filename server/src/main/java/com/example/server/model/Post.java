package com.example.server.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    @ManyToOne
    private final User authorId;
    @Temporal(TemporalType.TIMESTAMP)
    private final Date creationTime;
    @Column(length = 4096)
    private String content;
    private Integer postRating;

    public Post(User authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.creationTime = new Date();
        this.postRating = 0;
    }

    public Integer getPostId() {
        return postId;
    }

    public User getAuthorId() {
        return authorId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getContent() {
        return content;
    }

    public Integer getPostRating() {
        return postRating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void incrementRating() {
        this.postRating++;
    }

    public void decrementRating() {
        this.postRating--;
    }

}
