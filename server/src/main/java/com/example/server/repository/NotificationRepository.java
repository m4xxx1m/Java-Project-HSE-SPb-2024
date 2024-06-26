package com.example.server.repository;

import com.example.server.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByOriginalCommentAuthorId(int originalCommentAuthorId);

    List<Notification> findByOriginalCommentAuthorIdOrderByReplyCommentIdDesc(int originalCommentAuthorId);
    List<Notification> findByPostId(int postId);
    List<Notification> findByReplyCommentId(int replyCommentId);
    List<Notification> findByOriginalCommentId(int originalCommentId);
}
