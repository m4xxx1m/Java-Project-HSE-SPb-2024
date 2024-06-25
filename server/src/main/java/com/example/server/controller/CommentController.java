package com.example.server.controller;

import com.example.server.dto.ContentObjDto;
import com.example.server.model.Comment;
import com.example.server.model.SavedObject;
import com.example.server.service.CommentService;
import com.example.server.service.PostService;
import com.example.server.service.SavedObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class CommentController {

    private final CommentService commentService;

    private final SavedObjectService savedObjectService;

    @Autowired
    public CommentController(CommentService commentService, SavedObjectService savedObjectService) {
        this.commentService = commentService;
        this.savedObjectService = savedObjectService;
    }

    @RequestMapping("/post/{postId}/comments")
    ResponseEntity<List<Comment>> getComments(@PathVariable int postId, @RequestParam(defaultValue = "-1") int prevId,
                                              @RequestParam(defaultValue = "10") int size) {
        List<Comment> list = commentService.getCommentsAfterId(postId, prevId, size);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @RequestMapping("/post/{postId}/addComment")
    ResponseEntity<Comment> addComment(@PathVariable int postId, @RequestBody ContentObjDto commentRequest) {
        Comment comment = commentService.addComment(commentRequest, postId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @RequestMapping("/post/{postId}/comments/{commentId}/delete")
    ResponseEntity<Void> deleteComment(@PathVariable int commentId) {
        if (!Objects.equals(AuthController.getCurrentUserId(), commentService.getCommentById(commentId).getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/post/{postId}/comments/{commentId}/edit")
    ResponseEntity<Comment> editComment(@PathVariable int commentId, @RequestParam String content) {
        Comment comment = commentService.getCommentById(commentId);
        if (!Objects.equals(AuthController.getCurrentUserId(), comment.getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            commentService.editComment(comment, new ContentObjDto(comment.getAuthorId(), content, comment.getReplyToCommentId()));
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/post/{postId}/comments/{commentId}/like")
    ResponseEntity<Integer> likeComment(@PathVariable Integer commentId, @RequestParam("userId") int userId) {
        int rating = commentService.incrementCommentRating(commentId, userId);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @RequestMapping(value = "/post/{postId}/comments/{commentId}/dislike")
    ResponseEntity<Integer> dislikeComment(@PathVariable Integer commentId, @RequestParam("userId") int userId) {
        int rating = commentService.decrementCommentRating(commentId, userId);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @RequestMapping(value = "/saved/comments")
    ResponseEntity<List<Comment>> getSavedComments(@RequestParam("userId") int userId) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Integer> savedCommentIds = savedObjectService.getSavedObjectByUserId(userId, SavedObject.Type.COMMENT);
        List<Comment> savedComments = commentService.getCommentsByCommentIds(savedCommentIds);
        if (savedComments == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(savedComments, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/comments")
    ResponseEntity<List<Comment>> getUserComments(@RequestParam("userId") int userId) {
        List<Comment> userComments = commentService.getCommentsByAuthorId(userId);
        if (userComments == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userComments, HttpStatus.OK);
    }

}
