package com.example.lostlink.dto;

import com.example.lostlink.enums.ItemStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================
 * LostItemResponseDto — DTO for Lost Item Responses
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Returns item data to the client. Instead of exposing the full
 * User entity (reportedBy), we only include the reporter's name
 * and ID — this is called "projection" and prevents over-fetching.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LostItemResponseDto {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDateTime dateLost;
    private ItemStatus status;
    private String imageUrl;
    private Long reportedById;
    private String reportedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}