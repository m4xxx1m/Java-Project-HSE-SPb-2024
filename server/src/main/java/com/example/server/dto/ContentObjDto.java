package com.example.server.dto;

public class ContentObjDto {
    private final int authorId;
    private final String content;
    private final int replyToCommentId;

    public ContentObjDto(int authorId, String content, Integer replyToCommentId) {
        this.authorId = authorId;
        this.content = content;
        this.replyToCommentId = replyToCommentId == null ? -1 : replyToCommentId;
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
