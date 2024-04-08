package com.example.server.service;

import com.example.server.model.SavedPost;
import com.example.server.repository.SavedPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SavedPostService {
    @Autowired
    SavedPostRepository savedPostRepository;

    public void savePost(int userId, int postId) {
        savedPostRepository.save(new SavedPost(userId, postId));
    }

    public void deleteSavedPostForAllUsers(int postId) {
        List<SavedPost> postSavings = savedPostRepository.findByPostId(postId);
        for (SavedPost postSaving : postSavings) {
            savedPostRepository.deleteById(postSaving.getId());
        }
    }
}
