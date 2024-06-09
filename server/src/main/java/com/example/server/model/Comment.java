package com.example.server.model;


import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends ContentObj {

    private int postId;

    private Integer replyToCommentId;

    public Comment() {
        super();
    }

    public Comment(int postId, Integer replyToCommentId) {
        super();
        this.postId = postId;
        this.replyToCommentId = replyToCommentId;
    }

    public Comment(int authorId, String content, int postId, Integer replyToCommentId) {
        super(authorId, content);
        this.postId = postId;
        this.replyToCommentId = replyToCommentId;
    }

    public int getPostId() {
        return postId;
    }

    public Integer getReplyToCommentId() {
        return replyToCommentId;
    }

}
