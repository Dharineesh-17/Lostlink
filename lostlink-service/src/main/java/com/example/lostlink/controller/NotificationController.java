package com.example.lostlink.controller;

import com.example.lostlink.dto.ApiResponseDto;
import com.example.lostlink.entity.Notification;
import com.example.lostlink.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================================================
 * NotificationController — Notification REST Endpoints
 * ============================================================
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "User notification endpoints")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get current user's notifications")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<Notification>>> getNotifications(Authentication auth) {
        // In production, extract userId from auth token
        return ResponseEntity.ok(
                ApiResponseDto.success("Notifications retrieved", List.of()));
    }

    @Operation(summary = "Get unread notification count")
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponseDto<Long>> getUnreadCount(Authentication auth) {
        return ResponseEntity.ok(
                ApiResponseDto.success("Unread count retrieved", 0L));
    }

    @Operation(summary = "Mark a notification as read")
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponseDto<Void>> markAsRead(
            @PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(ApiResponseDto.success("Notification marked as read"));
    }
}