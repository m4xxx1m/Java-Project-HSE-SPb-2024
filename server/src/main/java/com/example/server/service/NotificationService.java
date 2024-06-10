package com.example.server.service;

import com.example.server.model.Notification;
import com.example.server.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(int userId) {
        return notificationRepository.findByOriginalCommentAuthorId(userId);
    }

    public void deleteNotification(int notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
