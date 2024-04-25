package com.example.server.controller;


import com.example.server.dto.PostDto;
import com.example.server.model.Post;
import com.example.server.model.SavedObject;
import com.example.server.service.PostService;
import com.example.server.service.SavedObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    private final SavedObjectService savedObjectService;

    @Autowired
    public PostController(PostService postService, SavedObjectService savedObjectService) {
        this.postService = postService;
        this.savedObjectService = savedObjectService;
    }

    @GetMapping("/post/getAll")
    ResponseEntity<List<Post>> getAll() {
        List<Post> list = postService.getPosts();
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @RequestMapping("/post/getAll")
    ResponseEntity<List<Post>> getPostsByTags(@RequestParam("tagIds") List<Integer> tagIds) {
        List<Post> list = postService.getPostsBySelectedTags(tagIds);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @RequestMapping("/post/add")
    ResponseEntity<Post> addPost(@RequestBody PostDto postDto) {
        Post post = postService.addPost(postDto);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping(value = "/post/{id}")
    ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }

    @RequestMapping("/post/{id}/edit")
    ResponseEntity<Post> editPost(@PathVariable Integer id, @RequestBody PostDto postDto) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            postService.editPost(post, postDto);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }

    @RequestMapping("/post/{id}/delete")
    ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        try {
            postService.deletePost(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @RequestMapping(value = "/post/{id}/like")
    ResponseEntity<Void> likePost(@PathVariable Integer id, @RequestParam("userId") int userId) {
        postService.incrementPostRating(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/post/{id}/dislike")
    ResponseEntity<Void> dislikePost(@PathVariable Integer id,  @RequestParam("userId") int userId) {
        postService.decrementPostRating(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/post/{id}/save")
    ResponseEntity<Void> savePost(@PathVariable Integer id, @RequestParam("userId") int userId) {
        savedObjectService.saveObject(userId, id, SavedObject.Type.POST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/saved/posts")
    ResponseEntity<List<Post>> getSavedPost(@RequestParam("userId") int userId) {
        List<Integer> savedPostIds = savedObjectService.getSavedObjectByUserId(userId, SavedObject.Type.POST);
        List<Post> savedPosts = postService.getPostsByPostIds(savedPostIds);
        if (savedPosts == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(savedPosts, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/posts")
    ResponseEntity<List<Post>> getUserPosts(@RequestParam("userId") int userId) {
        List<Post> userPosts = postService.getPostsByAuthorId(userId);
        if (userPosts == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postService.getPostsByAuthorId(userId), HttpStatus.OK);
    }

}
