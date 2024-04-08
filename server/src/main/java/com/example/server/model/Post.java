package com.example.server.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {

    @Column(length = 255)
    private String title;
    private List<Integer> commentIds = new ArrayList<>();
    private List<Integer> tagIds = new ArrayList<>();

    public Post() {
        super();
    }
    public Post(int authorId, String title, String content, List<Integer> tagIds) {
        super(authorId, content);
        this.title = title;
        this.tagIds = tagIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<Integer> commentIds) {
        this.commentIds = commentIds;
    }
    
    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

}
