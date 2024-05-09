package com.example.server.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostDto extends ContentObjDto {

    private final String tags;
    private final String title;
    private final int commentsCount;

    public PostDto(int authorId, String title, String content, String tags, int commentsCount) {
        super(authorId, content);
        this.tags = tags;
        this.title = title;
        this.commentsCount = commentsCount;
    }

    public String getTitle() {
        return title;
    }

    public String getTags() {
        return tags;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

}
