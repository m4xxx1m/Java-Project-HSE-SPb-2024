package com.example.server.model;


import jakarta.persistence.*;

@Entity
@Table(name = "post_files")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileName;
    private String key;
    private int postId;
    private String fileType;

    public FileInfo() {
    }

    public FileInfo(String fileName, String key, int postId, String fileType) {
        this.fileName = fileName;
        this.key = key;
        this.postId = postId;
        this.fileType = fileType;
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
    public String getFileType() {
        return fileType;
    }
}
