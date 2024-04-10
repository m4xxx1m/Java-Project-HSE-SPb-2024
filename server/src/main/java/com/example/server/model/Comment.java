package com.example.server.model;


import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends ContentObj {

    private int postId;

    public Comment() {
        super();
    }

    public Comment(int postId) {
        super();
        this.postId = postId;
    }

    public Comment(int authorId, String content, int postId) {
        super(authorId, content);
        this.postId = postId;
    }
    public int getPostId() {
        return postId;
    }
}
