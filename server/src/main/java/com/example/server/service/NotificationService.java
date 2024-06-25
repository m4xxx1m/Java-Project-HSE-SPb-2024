package com.example.server.service;

import com.example.server.dto.NotificationDto;
import com.example.server.model.Comment;
import com.example.server.model.Notification;
import com.example.server.repository.CommentRepository;
import com.example.server.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    CommentRepository commentRepository;

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(int userId) {
        return notificationRepository.findByOriginalCommentAuthorIdOrderByReplyCommentIdDesc(userId);
    }

    public List<NotificationDto> getNotificationsForUser(int userId) {
        List<Notification> notifications = getUserNotifications(userId);
        return notifications.stream().map(notification -> {
            Comment originalComment = commentRepository.findById(
                    notification.getOriginalCommentId()).orElse(null);
            Comment replyComment = commentRepository.findById(
                    notification.getReplyCommentId()).orElse(null);
            return new NotificationDto(notification.getId(), originalComment, replyComment);
        }).collect(Collectors.toList());
    }

    public void deleteNotification(int notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void deleteNotificationsByPostId(int id) {
        List<Notification> notifications = notificationRepository.findByPostId(id);
        notificationRepository.deleteAll(notifications);
    }

    public void deleteNotificationsByReplyCommentId(int id) {
        List<Notification> notifications = notificationRepository.findByReplyCommentId(id);
        notificationRepository.deleteAll(notifications);
    }

    public void deleteNotificationsByOriginalCommentId(int id) {
        List<Notification> notifications = notificationRepository.findByOriginalCommentId(id);
        notificationRepository.deleteAll(notifications);
    }
}
