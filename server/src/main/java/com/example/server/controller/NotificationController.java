package com.example.server.controller;


import com.example.server.model.Notification;
import com.example.server.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationService notificationService;

    public NotificationController(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @PostMapping
    public void notifyUserAboutReply(Integer userId, Notification notification) {
        messagingTemplate.convertAndSendToUser(userId.toString(), "/topic/replies", notification);
    }

    @GetMapping(value = "/{userId}")
    ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @DeleteMapping(value = "/{notificationId}/delete")
    ResponseEntity<Void> deleteNotification(@PathVariable Integer notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}
