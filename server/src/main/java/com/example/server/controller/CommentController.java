package com.example.server.controller;

import com.example.server.dto.ContentObjDto;
import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.service.CommentService;
import com.example.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post/{postId}")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @RequestMapping("/comments")
    ResponseEntity<List<Comment>> getComments(@PathVariable long postId) {
        return new ResponseEntity<>(commentService.getCommentsByPostId(postId), HttpStatus.OK);
    }

    @RequestMapping("/addComment")
    ResponseEntity<Comment> addComment(@PathVariable long postId, @RequestBody ContentObjDto commentRequest) {
        Comment comment = commentService.addComment(commentRequest, postId);
        postService.addCommentToPost(postId, comment.getId());
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }


    /*@RequestMapping("/addComment0")
    ResponseEntity<Comment> addComment(@PathVariable long postId, @RequestParam("authorId") long authorId, @RequestParam("content") String content) {
        ContentObjDto commentRequest = new ContentObjDto(authorId, content);
        Comment comment = commentService.addComment(commentRequest, postId);
        postService.addCommentToPost(postId, comment.getId());
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }*/

}
