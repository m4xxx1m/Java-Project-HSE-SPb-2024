package com.example.server.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {

    private String tags;
    @Column(length = 255)
    private String title;
    private int commentsCount = 0;

    public Post() {
        super();
    }
    public Post(int authorId, String title, String content, String tags, int commentsCount) {
        super(authorId, content);
        this.title = title;
        this.tags = tags;
        this.commentsCount = commentsCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
