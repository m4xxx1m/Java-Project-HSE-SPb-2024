package com.example.server.dto;

import java.util.ArrayList;
import java.util.List;

public class PostDto extends ContentObjDto {
    private List<Integer> tagIds = new ArrayList<>();
    private String title = "";

    public PostDto(int authorId, String title, String content, List<Integer> tagIds) {
        super(authorId, content);
        this.title = title;
        this.tagIds = tagIds;
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

}
