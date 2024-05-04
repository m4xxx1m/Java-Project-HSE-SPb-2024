package com.example.server.service;

import com.example.server.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final String DIRECTORY_PATH = "server\\src\\main\\resources\\files\\";

    @Autowired
    private FileInfoRepository fileInfoRepository;

    public FileInfo findById(int id) {
        return fileInfoRepository.findById(id).orElse(null);
    }

    public FileInfo upload(MultipartFile resource, int postId) throws IOException {
        String key = generateKey(resource.getOriginalFilename());
        FileInfo createdFile = new FileInfo(resource.getOriginalFilename(), key, postId, resource.getContentType());
        uploadFileData(resource.getBytes(), key);
        fileInfoRepository.save(createdFile);
        return createdFile;
    }

    private String generateKey(String name) {
        return DigestUtils.md5DigestAsHex((name + LocalDateTime.now()).getBytes());
    }

    private void uploadFileData(byte[] fileData, String keyName) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + keyName);
        Files.createDirectories(path.getParent());
        Path file = Files.createFile(path);
        try (FileOutputStream stream = new FileOutputStream(file.toString())) {
            stream.write(fileData);
        }
    }

    public byte[] download(String key) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + key);
        return Files.readAllBytes(path);
    }

    public void delete(FileInfo fileInfo) throws IOException {
        assert fileInfo != null;
        Path path = Paths.get(DIRECTORY_PATH + fileInfo.getKey());
        Files.delete(path);
    }

}
