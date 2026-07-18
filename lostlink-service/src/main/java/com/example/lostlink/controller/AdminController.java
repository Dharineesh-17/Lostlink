package com.example.lostlink.controller;

import com.example.lostlink.dto.ApiResponseDto;
import com.example.lostlink.dto.DashboardStatsDto;
import com.example.lostlink.dto.UserResponseDto;
import com.example.lostlink.entity.User;
import com.example.lostlink.enums.UserRole;
import com.example.lostlink.exception.ResourceNotFoundException;
import com.example.lostlink.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================================================
 * AdminController — Admin Management Endpoints
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Admin-only endpoints for managing users and system-wide operations.
 * @PreAuthorize ensures only users with ROLE_ADMIN can access these.
 * ============================================================
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin-only management endpoints")
public class AdminController {

    private final UserRepository userRepository;

    @Operation(summary = "Get all users (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
        return ResponseEntity.ok(ApiResponseDto.success("Users retrieved", users));
    }

    @Operation(summary = "Get user by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return ResponseEntity.ok(ApiResponseDto.success("User retrieved", mapToDto(user)));
    }

    @Operation(summary = "Update user role (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUserRole(
            @PathVariable Long id, @RequestParam String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setRole(UserRole.valueOf(role.toUpperCase()));
        User updated = userRepository.save(user);
        return ResponseEntity.ok(ApiResponseDto.success("User role updated", mapToDto(updated)));
    }

    @Operation(summary = "Delete a user (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponseDto.success("User deleted"));
    }

    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}