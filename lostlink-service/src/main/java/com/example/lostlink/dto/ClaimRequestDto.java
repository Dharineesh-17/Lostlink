package com.example.lostlink.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * ============================================================
 * ClaimRequestDto — DTO for Creating Claims
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * A student submits a claim with a reason (proof of ownership).
 * The "status" is NOT included — claims always start as PENDING.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimRequestDto {

    @NotNull(message = "Item ID is required")
    private Long itemId;

    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 1000, message = "Reason must be between 10 and 1000 characters")
    private String reason;
}