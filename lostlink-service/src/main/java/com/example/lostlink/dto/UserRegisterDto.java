package com.example.lostlink.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * ============================================================
 * UserRegisterDto — DTO for User Registration Requests
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * We NEVER expose the User entity directly in API requests/responses.
 * DTOs (Data Transfer Objects) serve as a boundary between the API
 * layer and the entity layer. Benefits:
 * - Validation: We validate the DTO, not the entity
 * - Security: Password from registration never reaches the entity's
 *   response DTO (no accidental password leaks)
 * - Flexibility: API fields can differ from DB columns
 * 
 * VALIDATION ANNOTATIONS:
 * @NotBlank    — String must not be null AND must contain at least one non-whitespace character
 * @Email       — Must be a valid email format (RFC 5322)
 * @Size        — Constrains string length (min/max)
 * @Pattern     — Must match the given regex (password complexity)
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}