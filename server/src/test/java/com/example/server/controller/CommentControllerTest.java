package com.example.server.controller;

import com.example.server.dto.ContentObjDto;
import com.example.server.model.Comment;
import com.example.server.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private RatedObjectService ratedObjectService;

    @MockBean
    private SavedObjectService savedObjectService;

    @MockBean
    private NotificationService notificationService;


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetComments() throws Exception {
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentService.getCommentsAfterId(1, -1, 10)).thenReturn(Collections.singletonList(comment));
        mockMvc.perform(MockMvcRequestBuilders.get("/post/1/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(comment))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/post/1/comments/1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testLikeComment() throws Exception {
        when(commentService.incrementCommentRating(1, 1)).thenReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.get("/post/1/comments/1/like?userId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDislikeComment() throws Exception {
        when(commentService.decrementCommentRating(1, 1)).thenReturn(-1);
        mockMvc.perform(MockMvcRequestBuilders.get("/post/1/comments/1/dislike?userId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("-1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testEditComment() throws Exception {
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentService.getCommentById(1)).thenReturn(comment);
        mockMvc.perform(MockMvcRequestBuilders.get("/post/1/comments/1/edit?content=content")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(comment)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddComment() throws Exception {
        Comment comment = new Comment(1, "content", 1, -1);
        when(commentService.addComment(any(ContentObjDto.class), eq(1))).thenReturn(comment);
        mockMvc.perform(MockMvcRequestBuilders.post("/post/1/addComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ContentObjDto(1, "content", -1)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(comment)));
    }

}
