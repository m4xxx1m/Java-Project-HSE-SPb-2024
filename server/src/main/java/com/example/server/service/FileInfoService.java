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

    @Autowired
    private FileInfoRepository fileInfoRepository;

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
        String DIRECTORY_PATH = "src/main/resources/files/";
        Path path = Paths.get(DIRECTORY_PATH, keyName);
        Path file = Files.createFile(path);
        try (FileOutputStream stream = new FileOutputStream(file.toString())) {
            stream.write(fileData);
        }
    }

}
