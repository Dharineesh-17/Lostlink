package com.example.lostlink.dto;

import com.example.lostlink.enums.ClaimStatus;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * ============================================================
 * ClaimStatusUpdateDto — DTO for Security Staff to Update Claims
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Security staff can approve or reject claims. This DTO
 * accepts the new status and an optional remark explaining the decision.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimStatusUpdateDto {

    @NotNull(message = "Status is required")
    private ClaimStatus status;

    private String remark;
}