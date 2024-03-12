package com.example.server.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;


@Entity
public class ContentObj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private final long authorId;
    @Temporal(TemporalType.TIMESTAMP)
    private final Date creationTime;
    @Column(length = 4096)
    private String content;
    private long rating;

    public ContentObj(long authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.creationTime = new Date();
        this.rating = 0;
    }

    public long getId() {
        return id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getContent() {
        return content;
    }

    public long getRating() {
        return rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void incrementRating() {
        this.rating++;
    }

    public void decrementRating() {
        this.rating--;
    }

}
