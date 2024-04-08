package com.example.server.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "saved_posts")
public class SavedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;

    private int postId;

    public SavedPost() {}

    public SavedPost(int userId, int postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }
}
