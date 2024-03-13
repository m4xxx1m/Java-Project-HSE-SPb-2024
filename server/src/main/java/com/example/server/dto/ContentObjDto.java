package com.example.server.dto;

public class ContentObjDto {
    private final long authorId;
    private final String content;

    public ContentObjDto(long authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

    public long getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return  content;
    }

}
