package com.example.server.service;

import com.example.server.controller.NotificationController;
import com.example.server.dto.ContentObjDto;
import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationController notificationController;

    @Mock
    private RatedObjectService ratedObjectService;

    @Mock
    private PostService postService;

    @Mock
    private SavedObjectService savedObjectService;

    @Test
    public void testGetCommentById() {
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        Comment resultComment = commentService.getCommentById(1);
        assertEquals(comment.getContent(), resultComment.getContent());
    }

    @Test
    public void testCreateComment() {
        Post post = new Post();
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentRepository.save(comment)).thenReturn(comment);
        Comment resultComment = commentService.addComment(new ContentObjDto(1, "content", -1), 1);
        assertEquals(comment.getContent(), resultComment.getContent());
    }

    @Test
    public void testDeleteComment() {
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        commentService.deleteComment(1);
    }

    @Test
    public void testEditComment() {
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        commentService.editComment(comment, new ContentObjDto(1, "new content", -1));
        Comment resultComment = commentService.getCommentById(1);
        assertEquals("new content", resultComment.getContent());
    }

    @Test
    public void testDecrementCommentRating() {
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        commentService.decrementCommentRating( 1, 1);
        assertEquals(-1, comment.getRating());
    }

    @Test
    public void testIncrementCommentRating() {
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        commentService.incrementCommentRating( 1, 1);
        assertEquals(1, comment.getRating());
    }

    @Test
    public void testGetCommentsByAuthorId() {
        Comment comment1 = new Comment(1, "content1", 1, -1);
        Comment comment2 = new Comment(2, "content2", 1, -1);
        when(commentRepository.findByAuthorId(1)).thenReturn(List.of(comment1, comment2));
        List<Comment> resultComments = commentService.getCommentsByAuthorId(1);
        assertEquals(2, resultComments.size());
    }

}
