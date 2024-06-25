package com.example.server.controller;

import com.example.server.dto.JwtAuthenticationResponse;
import com.example.server.dto.UserLoginDto;
import com.example.server.dto.UserRegistrationDto;
import com.example.server.model.User;
import com.example.server.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUser() throws Exception {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("newUser");
        registrationDto.setEmail("newEmail@example.com");
        registrationDto.setPassword("newPassword");

        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse("token", new User());

        doReturn(jwtResponse).when(authenticationService).signUp(any(UserRegistrationDto.class));

        mockMvc.perform(post("/auth/sign-up")
                        .content(objectMapper.writeValueAsString(registrationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    @Test
    public void testSignInUser() throws Exception {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsername("existingUser");
        loginDto.setPassword("existingPassword");

        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse("token", new User());

        when(authenticationService.signIn(any(UserLoginDto.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/sign-in")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }
}