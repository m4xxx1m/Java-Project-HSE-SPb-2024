package com.example.server.service;

import com.example.server.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import com.example.server.repository.FileInfoRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.util.DigestUtils;


@Service
public class FileInfoService {

    private final String DIRECTORY_PATH = "src/main/resources/files/";

    @Autowired
    private FileInfoRepository fileInfoRepository;

    public FileInfo findById(int id) {
        return fileInfoRepository.findById(id).orElse(null);
    }

    public FileInfo upload(MultipartFile resource, int postId) throws IOException {
        String key = generateKey(resource.getName());
        FileInfo createdFile = new FileInfo(resource.getOriginalFilename(), resource.getSize(), key, postId);
        fileInfoRepository.save(createdFile);
        uploadFileData(resource.getBytes(), key);
        return createdFile;
    }

    private String generateKey(String name) {
        return DigestUtils.md5DigestAsHex((name + LocalDateTime.now()).getBytes());
    }

    private void uploadFileData(byte[] fileData, String keyName) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH, keyName);
        Path file = Files.createFile(path);
        try (FileOutputStream stream = new FileOutputStream(file.toString())) {
            stream.write(fileData);
        }
    }

    public Resource download(String key) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + key);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException();
        }
    }

    public void delete(int fileInfoId) throws IOException {
        FileInfo fileInfo = fileInfoRepository.findById(fileInfoId).orElse(null);
        assert fileInfo != null;
        Path path = Paths.get(DIRECTORY_PATH + fileInfo.getKey());
        Files.delete(path);
    }

    // TODO - requests
    // TODO - connection with posts

}
