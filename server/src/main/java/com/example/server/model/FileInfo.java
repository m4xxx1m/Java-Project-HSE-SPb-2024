package com.example.server.model;


import jakarta.persistence.*;

@Entity
@Table(name = "file_info")
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileName;
    private String key;
    private int sourceId;
    private String fileType;

    public FileInfo() {
    }

    public FileInfo(String fileName, String key, int sourceId, String fileType) {
        this.fileName = fileName;
        this.key = key;
        this.sourceId = sourceId;
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

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
    public String getFileType() {
        return fileType;
    }
}
