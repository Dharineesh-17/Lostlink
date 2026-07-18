package com.example.lostlink.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * ============================================================
 * LostItemRequestDto — DTO for Creating/Updating Lost Items
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Accepts input from the client when reporting a lost or found item.
 * Includes validation rules so invalid data never reaches the service layer.
 * The "status" is intentionally excluded — it's set by the system,
 * not the user (prevents users from marking items as RETURNED directly).
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LostItemRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    private LocalDateTime dateLost;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;
}