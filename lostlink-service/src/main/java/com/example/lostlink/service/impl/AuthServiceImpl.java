package com.example.lostlink.service.impl;

import com.example.lostlink.dto.*;
import com.example.lostlink.entity.User;
import com.example.lostlink.enums.UserRole;
import com.example.lostlink.exception.ResourceNotFoundException;
import com.example.lostlink.repository.UserRepository;
import com.example.lostlink.security.JwtTokenProvider;
import com.example.lostlink.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ============================================================
 * AuthServiceImpl — Authentication Service Implementation
 * ============================================================
 *
 * WHY THIS EXISTS:
 * Implements the business logic for user registration and login.
 * This is where we:
 * 1. Validate business rules (password match, duplicate email)
 * 2. Hash passwords with BCrypt
 * 3. Assign default roles
 * 4. Generate JWT tokens
 *
 * @Service — Marks this as a Spring-managed service bean.
 *   Spring creates a single instance and injects it wherever needed.
 * @Transactional — Wraps methods in a database transaction.
 *   If anything fails, the transaction rolls back automatically.
 *   (readOnly=true) for methods that only read data — optimizes performance.
 * @RequiredArgsConstructor — Lombok: generates constructor for all final fields
 *   Spring uses this constructor for dependency injection.
 * @Slf4j — Lombok: generates a logger named "log"
 * ============================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registers a new user.
     *
     * BUSINESS RULES:
     * 1. Password and confirmPassword must match
     * 2. Email must be unique (check DB)
     * 3. Password is hashed before storing (never store plain text!)
     * 4. Default role is STUDENT
     */
    @Override
    @Transactional
    public UserResponseDto registerUser(UserRegisterDto registerDto) {
        log.info("Registering new user with email: {}", registerDto.getEmail());

        // Business validation: password confirmation
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Business validation: unique email
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + registerDto.getEmail());
        }

        // Build the User entity
        User user = User.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword())) // BCrypt hash
                .role(UserRole.STUDENT) // Default role for new registrations
                .build();

        // Save to database
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Convert to DTO (never return the entity directly)
        return mapToResponseDto(savedUser);
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * FLOW:
     * 1. Spring Security's AuthenticationManager verifies the credentials
     * 2. If valid, we generate a JWT containing the user's email and role
     * 3. Return the token along with user info
     *
     * If authentication fails, AuthenticationManager throws
     * BadCredentialsException (handled by GlobalExceptionHandler).
     */
    @Override
    public JwtAuthResponseDto loginUser(UserLoginDto loginDto) {
        log.info("User login attempt for email: {}", loginDto.getEmail());

        // Step 1: Authenticate using Spring Security
        // DaoAuthenticationProvider (auto-configured) uses our
        // CustomUserDetailsService and BCryptPasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        // Step 2: Generate JWT
        String token = jwtTokenProvider.generateToken(authentication);

        // Step 3: Load user for response
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loginDto.getEmail()));

        log.info("User logged in successfully: {}", loginDto.getEmail());

        return JwtAuthResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(86400000L) // 24 hours in ms
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    /**
     * Gets the current authenticated user's profile.
     * Uses the email from the JWT token (stored in Authentication).
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToResponseDto(user);
    }

    /**
     * Converts a User entity to a UserResponseDto.
     *
     * WHY A SEPARATE METHOD?
     * This mapping logic might be reused across multiple service methods.
     * Keeping it in one place ensures consistency and easy updates.
     */
    private UserResponseDto mapToResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}