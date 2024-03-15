package com.example.server.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;


@Entity
public class ContentObj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int authorId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Column(length = 4096)
    private String content;
    private int rating;

    public ContentObj() {}

    public ContentObj(int authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.creationTime = new Date();
        this.rating = 0;
    }

    public int getId() {
        return id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
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

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

}
