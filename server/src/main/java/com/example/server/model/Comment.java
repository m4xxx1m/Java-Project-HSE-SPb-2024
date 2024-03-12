package com.example.server.model;


import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment extends ContentObj {

    private final long postId;

    public Comment(long authorId, String content, long postId) {
        super(authorId, content);
        this.postId = postId;
    }
    public long getPostId() {
        return postId;
    }
}
