package com.example.server.controller;

import com.example.server.model.Subscription;
import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import com.example.server.service.AuthenticationService;
import com.example.server.service.SubscriptionService;
import com.example.server.service.UserService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetUsersList() throws Exception {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        when(userService.getUsersList(new HashSet<>(Arrays.asList(1, 2)))).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(post("/users/getUsersList")
                        .content(objectMapper.writeValueAsString(new HashSet<>(Arrays.asList(1, 2))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(user1, user2))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetUser() throws Exception {
        User user = new User();
        user.setUsername("user1");

        when(userService.getUser(1)).thenReturn(user);

        mockMvc.perform(get("/users/getUser/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSubscribe() throws Exception {
        Subscription subscription = new Subscription();

        when(subscriptionService.subscribe(1, 2)).thenReturn(subscription);

        mockMvc.perform(post("/users/1/subscribe/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(subscription)));
    }
}