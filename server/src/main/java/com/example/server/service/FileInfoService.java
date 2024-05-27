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

    private static final String DIRECTORY_PATH = "src\\main\\resources\\files\\";

    @Autowired
    private FileInfoRepository fileInfoRepository;

    public FileInfo findById(Integer id) {
        return fileInfoRepository.findById(id).orElse(null);
    }

    public FileInfo upload(MultipartFile resource, int postId) throws IOException {
        String key = generateKey(resource.getOriginalFilename());
        FileInfo createdFile = new FileInfo(resource.getOriginalFilename(), key, postId, resource.getContentType());
        uploadFileData(resource.getBytes(), DIRECTORY_PATH + postId + "\\" + key);
        fileInfoRepository.save(createdFile);
        return createdFile;
    }

    static String generateKey(String name) {
        return DigestUtils.md5DigestAsHex((name + LocalDateTime.now()).getBytes());
    }

    static void uploadFileData(byte[] fileData, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Path file = Files.createFile(path);
        try (FileOutputStream stream = new FileOutputStream(file.toString())) {
            stream.write(fileData);
        }
    }

    static byte[] download(String key) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + key);
        return Files.readAllBytes(path);
    }

    void delete(int postId, FileInfo fileInfo) throws IOException {
        assert fileInfo != null;
        Path path = Paths.get(DIRECTORY_PATH + postId + "\\" + fileInfo.getKey());
        Files.delete(path);
        fileInfoRepository.delete(fileInfo);
    }

}