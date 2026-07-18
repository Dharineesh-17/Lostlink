package com.example.lostlink.repository;

import com.example.lostlink.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================
 * NotificationRepository — Repository for Notification Entity
 * ============================================================
 *
 * WHY THIS EXISTS:
 * Provides data access for user notifications. Users can view
 * their notification feed and mark individual notifications
 * as read. The repository supports fetching a user's notifications
 * in reverse chronological order (newest first) and counting
 * unread notifications for badge display in the UI.
 *
 * KEY METHODS:
 * - findByUserIdOrderByCreatedAtDesc() — Notification feed
 * - countByUserIdAndReadFalse() — Unread badge count
 * ============================================================
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Finds all notifications for a specific user, ordered by
     * newest first (descending creation timestamp).
     * Used to populate the user's notification feed.
     *
     * Spring Data JPA automatically generates:
     *   SELECT * FROM notifications WHERE user_id = ?
     *   ORDER BY created_at DESC
     *
     * @param userId the ID of the user whose notifications to retrieve
     * @return list of notifications for the given user, newest first
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Counts unread notifications for a user.
     * Used to show a badge count (e.g., a red dot with "3")
     * in the UI navigation bar.
     *
     * Spring Data JPA automatically generates:
     *   SELECT COUNT(*) FROM notifications
     *   WHERE user_id = ? AND read = false
     *
     * @param userId the ID of the user whose unread count to retrieve
     * @return number of unread notifications for the given user
     */
    long countByUserIdAndReadFalse(Long userId);
}