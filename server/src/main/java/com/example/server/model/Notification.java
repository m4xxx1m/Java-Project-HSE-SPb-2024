package com.example.server.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int postId;
    private int originalCommentId;
    private int originalCommentAuthorId;
    private int replyCommentId;
    private int replyCommentAuthorId;

    public Notification(int postId, int originalCommentId, int originalCommentAuthorId, int replyCommentId, int replyCommentAuthorId) {
        this.postId = postId;
        this.originalCommentId = originalCommentId;
        this.originalCommentAuthorId = originalCommentAuthorId;
        this.replyCommentId = replyCommentId;
        this.replyCommentAuthorId = replyCommentAuthorId;
    }
}
