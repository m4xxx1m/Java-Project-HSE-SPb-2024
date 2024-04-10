package com.example.server.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {

    private List<Integer> tagIds = new ArrayList<>();

    @Column(length = 255)
    private String title;

    public Post() {
        super();
    }
    public Post(int authorId, String title, String content, List<Integer> tagIds) {
        super(authorId, content);
        this.title = title;
        this.tagIds = tagIds;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
