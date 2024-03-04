package com.example.server.service;


import com.example.server.model.Post;
import com.example.server.model.User;
import com.example.server.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void addPost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    public Post getPostById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getPostsByAuthorId(User authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

}
