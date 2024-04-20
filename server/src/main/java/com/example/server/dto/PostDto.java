package com.example.server.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class PostDto extends ContentObjDto {

    private final List<Integer> tagIds;
    private final String title;
    private final int commentsCount;

    private final MultipartFile file;

    public PostDto(int authorId, String title, String content, List<Integer> tagIds, int commentsCount, MultipartFile file) {
        super(authorId, content);
        this.tagIds = tagIds;
        this.title = title;
        this.commentsCount = commentsCount;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public MultipartFile getFile() {
        return file;
    }

}
