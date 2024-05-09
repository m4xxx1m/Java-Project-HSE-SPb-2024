package com.example.server.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {

    private List<Integer> tagIds = new ArrayList<>();

    private String title;
    private int commentsCount;

    private Integer fileInfoId;

    private List<Integer> picInfoIds;

    public Post() {
        super();
    }
    public Post(int authorId, String title, String content, List<Integer> tagIds) {
        super(authorId, content);
        this.title = title;
        this.tagIds = tagIds;
        this.commentsCount = 0;
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

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public void incrementCommentsCount() {
        commentsCount++;
    }

    public void decrementCommentsCount() {
        commentsCount--;
    }

    public Integer getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(Integer fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

    public List<Integer> getPicInfoIds() {
        return picInfoIds;
    }

    public void setPicInfoIds(List<Integer> picInfoIds) {
        this.picInfoIds = picInfoIds;
    }

}
