package com.example.lostlink.service;

import com.example.lostlink.dto.*;
import org.springframework.security.core.Authentication;

/**
 * ============================================================
 * AuthService — Authentication Business Logic Interface
 * ============================================================
 *
 * WHY AN INTERFACE?
 * Following the "Program to an interface, not an implementation" principle.
 * Benefits:
 * - Controllers depend on the interface, not the impl class
 * - Easy to swap implementations (e.g., for testing with a mock)
 * - Clear contract: any implementation must provide these methods
 * ============================================================
 */
public interface AuthService {

    /**
     * Registers a new user in the system.
     * @param registerDto registration data with validation already applied
     * @return UserResponseDto with the created user's info (no password)
     */
    UserResponseDto registerUser(UserRegisterDto registerDto);

    /**
     * Authenticates a user and returns a JWT token.
     * @param loginDto login credentials
     * @return JWT response with access token and user info
     */
    JwtAuthResponseDto loginUser(UserLoginDto loginDto);

    /**
     * Gets the current authenticated user's profile.
     * @param authentication the Spring Security authentication object
     * @return UserResponseDto with current user's info
     */
    UserResponseDto getCurrentUser(Authentication authentication);
}