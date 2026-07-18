package com.example.lostlink.service;

import com.example.lostlink.entity.Notification;

import java.util.List;

/**
 * ============================================================
 * NotificationService — Notification Business Logic Interface
 * ============================================================
 */
public interface NotificationService {

    Notification createNotification(Long userId, String message, String type);

    List<Notification> getUserNotifications(Long userId);

    long getUnreadCount(Long userId);

    void markAsRead(Long notificationId, Long userId);
}