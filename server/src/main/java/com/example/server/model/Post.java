package com.example.server.model;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {

    private List<Integer> tagIds = new ArrayList<>();
    @Column(length = 255)
    private String title;
    private int commentsCount;
    private int fileInfoId;

    public Post() {
        super();
    }
    public Post(int authorId, String title, String content, List<Integer> tagIds) {
        super(authorId, content);
        this.title = title;
        this.tagIds = tagIds;
        this.commentsCount = 0;
        this.fileInfoId = 0;
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

    public int getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(int fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

}
