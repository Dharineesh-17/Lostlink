package com.example.lostlink.dto;

import com.example.lostlink.enums.UserRole;
import lombok.*;

/**
 * ============================================================
 * JwtAuthResponseDto — DTO for Login Response (JWT Token)
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * After successful login, the server returns a JWT token.
 * The client stores this token and sends it in the Authorization
 * header for all subsequent requests.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponseDto {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private Long userId;
    private String email;
    private UserRole role;
}