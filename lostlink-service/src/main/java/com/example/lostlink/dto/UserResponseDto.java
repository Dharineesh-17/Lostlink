package com.example.lostlink.dto;

import com.example.lostlink.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================
 * UserResponseDto — DTO for User Responses
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * This is what we return to the client after registration or
 * profile fetch. Notice: NO password field! We never expose
 * the hashed password in any response DTO.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
}