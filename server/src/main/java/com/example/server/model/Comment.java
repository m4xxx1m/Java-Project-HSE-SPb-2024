package com.example.server.model;


import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends ContentObj {

    private int postId;

    private int replyToCommentId;

    public Comment() {
        super();
    }

    public Comment(int postId, int replyToCommentId) {
        super();
        this.postId = postId;
        this.replyToCommentId = replyToCommentId;
    }

    public Comment(int authorId, String content, int postId, int replyToCommentId) {
        super(authorId, content);
        this.postId = postId;
        this.replyToCommentId = replyToCommentId;
    }
    public int getPostId() {
        return postId;
    }

    public int getReplyToCommentId() {
        return replyToCommentId;
    }

}
