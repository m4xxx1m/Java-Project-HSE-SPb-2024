package com.example.server.controller;

import com.example.server.dto.NotificationDto;
import com.example.server.model.Comment;
import com.example.server.model.Notification;
import com.example.server.service.NotificationService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetNotifications() throws Exception {
        Notification notification = new Notification(1, 2, 1, 3, 2);
        when(notificationService.getUserNotifications(1)).thenReturn(Collections.singletonList(notification));
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(notification))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetNotificationsForUser() throws Exception {
        NotificationDto notificationDto = new NotificationDto(1, new Comment(), new Comment());
        when(notificationService.getNotificationsForUser(1)).thenReturn(Collections.singletonList(notificationDto));
        mockMvc.perform(MockMvcRequestBuilders.get("/notifications/1/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(notificationDto))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteNotification() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
