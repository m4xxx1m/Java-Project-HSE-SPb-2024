package com.example.server.model;


import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment extends ContentObj {

    private long postId;

    public Comment() {
        super();
    }

    public Comment(long postId) {
        super();
        this.postId = postId;
    }

    public Comment(long authorId, String content, long postId) {
        super(authorId, content);
        this.postId = postId;
    }
    public long getPostId() {
        return postId;
    }
}
