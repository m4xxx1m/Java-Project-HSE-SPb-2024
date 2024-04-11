package com.example.server.dto;

public class ContentObjDto {
    private final int authorId;
    private final String content;

    public ContentObjDto(int authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return  content;
    }

}
