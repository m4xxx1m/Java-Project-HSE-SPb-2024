package com.example.server.dto;

public class ContentObjDto {
    private final int authorId;
    private final String content;
    private final int replyToCommentId;

    public ContentObjDto(int authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.replyToCommentId = -1;
    }

    public ContentObjDto(int authorId, String content, int replyToCommentId) {
        this.authorId = authorId;
        this.content = content;
        this.replyToCommentId = replyToCommentId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return  content;
    }

    public int getReplyToCommentId() {
        return replyToCommentId;
    }

}
