package com.example.server.controller;


import com.example.server.dto.ContentObjDto;
import com.example.server.model.Post;
import com.example.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /*@RequestMapping("/add0")
    ResponseEntity<Post> addPost(@RequestParam("authorId") long authorId, @RequestParam("content") String content) {
        ContentObjDto postDto = new ContentObjDto(authorId, content);
        Post post = postService.addPost(postDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }*/

    @RequestMapping("/add")
    ResponseEntity<Post> addPost(@RequestBody ContentObjDto postDto) {
        Post post = postService.addPost(postDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Post>  getPostById(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }

}
