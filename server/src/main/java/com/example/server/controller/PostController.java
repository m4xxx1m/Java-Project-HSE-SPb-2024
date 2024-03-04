package com.example.server.controller;


import com.example.server.model.Post;
import com.example.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    public String addPost() {
        return "addPost";
    }

    @PostMapping("/post/add")
    public String addPost(@RequestBody Post newPost) {
        postService.addPost(newPost);
        return "redirect:/";
    }

}
