package com.example.server.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {
    private List<Integer> tagIds = new ArrayList<>();

    public Post() {
        super();
    }
    public Post(int authorId, String content, List<Integer> tagIds) {
        super(authorId, content);
        this.tagIds = tagIds;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

}
