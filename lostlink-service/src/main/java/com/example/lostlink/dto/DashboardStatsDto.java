package com.example.lostlink.dto;

import lombok.*;

/**
 * ============================================================
 * DashboardStatsDto — DTO for Dashboard Statistics
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * The dashboard displays aggregated counts (total users, items,
 * claims). Instead of returning raw numbers, we wrap them in a
 * typed DTO for clarity and extensibility.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDto {

    private long totalUsers;
    private long totalLostItems;
    private long totalFoundItems;
    private long totalPendingClaims;
    private long totalClaimedItems;
    private long totalReturnedItems;
}