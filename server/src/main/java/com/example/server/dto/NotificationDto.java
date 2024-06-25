package com.example.server.dto;

import com.example.server.model.Comment;
import com.example.server.model.User;

public class NotificationDto {
    private final int id;
    private final Comment originalComment;
    private final Comment replyComment;

    public NotificationDto(
            int id,
            Comment originalComment,
            Comment replyComment) {
        this.id = id;
        this.originalComment = originalComment;
        this.replyComment = replyComment;
    }

    public int getId() {
        return id;
    }

    public Comment getOriginalComment() {
        return originalComment;
    }

    public Comment getReplyComment() {
        return replyComment;
    }

}
