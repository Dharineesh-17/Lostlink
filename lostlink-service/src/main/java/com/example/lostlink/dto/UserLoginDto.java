package com.example.lostlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * ============================================================
 * UserLoginDto — DTO for Login Requests
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Separates login input from the registration flow.
 * Only email + password needed — no name or confirm password.
 * Keeps each API endpoint's input contract clean and minimal.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}