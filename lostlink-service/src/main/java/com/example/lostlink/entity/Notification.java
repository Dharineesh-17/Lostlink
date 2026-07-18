package com.example.lostlink.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================
 * Notification — JPA Entity for user notifications
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Users need to be informed about events affecting them:
 * - "Your claim on [Item] has been approved"
 * - "A found item matching your lost report has been posted"
 * - "Your lost item report has been updated by security"
 * 
 * This entity stores these notifications in the database so
 * they persist across sessions and can be displayed in the
 * student/admin dashboard.
 * ============================================================
 */
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String message;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String type = "INFO";

    private boolean read;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}