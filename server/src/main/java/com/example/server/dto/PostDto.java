package com.example.server.dto;

import java.util.ArrayList;
import java.util.List;

public class PostDto extends ContentObjDto {
    private List<Integer> tagIds = new ArrayList<>();

    public PostDto(int authorId, String content, List<Integer> tagIds) {
        super(authorId, content);
        this.tagIds = tagIds;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

}
