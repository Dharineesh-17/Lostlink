package com.example.lostlink.dto;

import com.example.lostlink.enums.ItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * ============================================================
 * ItemStatusUpdateDto — DTO for Updating Item Status
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Security staff and admins can update item status (e.g., mark
 * a lost item as FOUND when someone turns it in).
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemStatusUpdateDto {

    @NotNull(message = "Status is required")
    private ItemStatus status;
}