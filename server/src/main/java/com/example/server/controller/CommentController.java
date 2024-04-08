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

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping("/comments")
    ResponseEntity<List<Comment>> getComments(@PathVariable int postId) {
        return new ResponseEntity<>(commentService.getCommentsByPostId(postId), HttpStatus.OK);
    }

    @RequestMapping("/addComment")
    ResponseEntity<Comment> addComment(@PathVariable int postId, @RequestBody ContentObjDto commentRequest) {
        Comment comment = commentService.addComment(commentRequest, postId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @RequestMapping("/comments/{commentId}/delete")
    ResponseEntity<Void> deleteComment(@PathVariable int commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/comments/{commentId}/edit")
    ResponseEntity<Comment> editComment(@PathVariable int commentId, @RequestParam String content) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            commentService.editComment(comment, new ContentObjDto(comment.getAuthorId(), content));
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/comments/{commentId}/like")
    ResponseEntity<Void> likeComment(@PathVariable Integer id) {
        commentService.incrementCommentRating(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/comments/{commentId}/dislike")
    ResponseEntity<Void> dislikeComment(@PathVariable Integer id) {
        commentService.decrementCommentRating(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
