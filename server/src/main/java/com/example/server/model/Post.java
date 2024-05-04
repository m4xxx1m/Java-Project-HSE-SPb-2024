package com.example.server.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {

    private List<Integer> tagIds = new ArrayList<>();

    private String title;
    private int commentsCount;

    @OneToOne
    private FileInfo fileInfo;

    public Post() {
        super();
    }
    public Post(int authorId, String title, String content, List<Integer> tagIds, FileInfo fileInfo) {
        super(authorId, content);
        this.title = title;
        this.tagIds = tagIds;
        this.commentsCount = 0;
        this.fileInfo = fileInfo;
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

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

}
