package com.example.lostlink.service;

import com.example.lostlink.dto.DashboardStatsDto;

/**
 * ============================================================
 * DashboardService — Dashboard Statistics Interface
 * ============================================================
 */
public interface DashboardService {

    DashboardStatsDto getAdminStats();

    DashboardStatsDto getSecurityStats();

    DashboardStatsDto getStudentStats(String email);
}