package com.example.server.dto;

import java.util.ArrayList;
import java.util.List;

public class PostDto extends ContentObjDto {

    private final List<Integer> tagIds;
    private final String title;

    public PostDto(int authorId, String title, String content, List<Integer> tagIds) {
        super(authorId, content);
        this.tagIds = tagIds;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

}
