package com.example.server.model;


import jakarta.persistence.*;

@Entity
@Table(name = "post_files")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileName;
    private long size;
    private String key;
    private int postId;

    public FileInfo() {
    }

    public FileInfo(String fileName, long size, String key, int postId) {
        this.fileName = fileName;
        this.size = size;
        this.key = key;
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
