package com.example.lostlink.controller;

import com.example.lostlink.dto.ApiResponseDto;
import com.example.lostlink.dto.DashboardStatsDto;
import com.example.lostlink.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================
 * DashboardController — Dashboard Statistics Endpoints
 * ============================================================
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard statistics endpoints")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get admin dashboard stats (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<ApiResponseDto<DashboardStatsDto>> getAdminStats() {
        return ResponseEntity.ok(
                ApiResponseDto.success("Admin stats retrieved", dashboardService.getAdminStats()));
    }

    @Operation(summary = "Get security dashboard stats (SECURITY/ADMIN only)")
    @PreAuthorize("hasAnyRole('SECURITY', 'ADMIN')")
    @GetMapping("/security")
    public ResponseEntity<ApiResponseDto<DashboardStatsDto>> getSecurityStats() {
        return ResponseEntity.ok(
                ApiResponseDto.success("Security stats retrieved", dashboardService.getSecurityStats()));
    }

    @Operation(summary = "Get student dashboard stats")
    @GetMapping("/student")
    public ResponseEntity<ApiResponseDto<DashboardStatsDto>> getStudentStats(Authentication auth) {
        return ResponseEntity.ok(
                ApiResponseDto.success("Student stats retrieved", dashboardService.getStudentStats(auth.getName())));
    }
}