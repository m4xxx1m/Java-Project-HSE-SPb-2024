package com.example.server.controller;


import com.example.server.dto.PostDto;
import com.example.server.model.Post;
import com.example.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/getAll")
    ResponseEntity<List<Post>> getAll() {
        List<Post> list = postService.getPosts();
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @RequestMapping("/add")
    ResponseEntity<Post> addPost(@RequestBody PostDto postDto) {
        Post post = postService.addPost(postDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }

    @RequestMapping("/{id}/edit")
    ResponseEntity<Post> editPost(@PathVariable Integer id, @RequestBody PostDto postDto) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            postService.editPost(post, postDto);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }

    @RequestMapping("/{id}/delete")
    ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
