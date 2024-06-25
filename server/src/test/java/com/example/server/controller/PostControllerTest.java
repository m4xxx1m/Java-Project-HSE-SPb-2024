package com.example.server.controller;

import com.example.server.dto.ContentObjDto;
import com.example.server.model.Comment;
import com.example.server.model.Post;
import com.example.server.model.SavedObject;
import com.example.server.repository.CommentRepository;
import com.example.server.repository.PostRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private SavedObjectService savedObjectService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private RatedObjectService ratedObjectService;

    @MockBean
    private FileInfoService fileInfoService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAll() throws Exception {
        Post post1 = new Post();
        post1.setTitle("post1");
        Post post2 = new Post();
        post2.setTitle("post2");

        when(postService.getPostsAfterId(-1, 10)).thenReturn(Arrays.asList(post1, post2));

        mockMvc.perform(MockMvcRequestBuilders.get("/post/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(post1, post2))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeletePost() throws Exception {
        doNothing().when(postService).deletePost(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/post/1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testLikePost() throws Exception {
        when(postService.getPostById(1)).thenReturn(new Post(1, "title", "content", "tags", 0));
        when(postService.incrementPostRating(1, 1)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/post/1/like")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(1)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDislikePost() throws Exception {
        when(postService.decrementPostRating(1, 1)).thenReturn(-1);

        mockMvc.perform(MockMvcRequestBuilders.post("/post/1/dislike")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(-1)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetSavedPost() throws Exception {
        when(savedObjectService.getSavedObjectByUserId(1, SavedObject.Type.POST)).thenReturn(Collections.singletonList(1));
        when(postService.getPostsByPostIds(anyList())).thenReturn(Collections.singletonList(new Post()));

        mockMvc.perform(MockMvcRequestBuilders.get("/saved/posts")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(new Post()))));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetPostByUserId() throws Exception {
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postService.getPostsByAuthorId(1)).thenReturn(Collections.singletonList(post));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/posts")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(post))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddPost() throws Exception {
        Post post = new Post(1, "title", "content", "tags", 0);
        when(postService.addPost(any(com.example.server.dto.PostDto.class))).thenReturn(post);
        mockMvc.perform(MockMvcRequestBuilders.post("/post/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new com.example.server.dto.PostDto(1, "title", "content", "tags", 0, false)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(post)));
    }

}