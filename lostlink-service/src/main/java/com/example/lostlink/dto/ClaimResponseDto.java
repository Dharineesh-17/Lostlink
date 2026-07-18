package com.example.lostlink.dto;

import com.example.lostlink.enums.ClaimStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================
 * ClaimResponseDto — DTO for Claim Responses
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimResponseDto {

    private Long id;
    private LocalDateTime claimDate;
    private String reason;
    private ClaimStatus status;
    private Long studentId;
    private String studentName;
    private Long itemId;
    private String itemTitle;
    private LocalDateTime createdAt;
}