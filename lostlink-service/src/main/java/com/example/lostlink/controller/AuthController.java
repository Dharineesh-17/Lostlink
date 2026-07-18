package com.example.lostlink.controller;

import com.example.lostlink.dto.*;
import com.example.lostlink.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================
 * AuthController — Authentication REST Endpoints
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Provides HTTP endpoints for user registration and login.
 * These are the ONLY public endpoints (no JWT required).
 * All other endpoints require a valid JWT in the Authorization header.
 * 
 * @RestController — Combines @Controller + @ResponseBody.
 *   Every method's return value is automatically serialized to JSON.
 * @RequestMapping — Base URL prefix for all endpoints in this controller.
 * @RequiredArgsConstructor — Lombok: injects AuthService
 * @Tag — Swagger: groups these endpoints under "Authentication" in the UI
 * ============================================================
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login endpoints")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register — Register a new user.
     * 
     * @Valid triggers Bean Validation on the request body.
     * If validation fails, MethodArgumentNotValidException is thrown
     * and caught by GlobalExceptionHandler.
     */
    @Operation(
        summary = "Register a new user",
        description = "Creates a new student account. Default role is STUDENT."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> register(
            @Valid @RequestBody UserRegisterDto registerDto) {
        UserResponseDto response = authService.registerUser(registerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("User registered successfully", response));
    }

    /**
     * POST /api/auth/login — Authenticate and get JWT token.
     */
    @Operation(
        summary = "Login user",
        description = "Authenticates credentials and returns a JWT access token."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<JwtAuthResponseDto>> login(
            @Valid @RequestBody UserLoginDto loginDto) {
        JwtAuthResponseDto response = authService.loginUser(loginDto);
        return ResponseEntity.ok(ApiResponseDto.success("Login successful", response));
    }

    /**
     * GET /api/auth/profile — Get current user's profile (requires JWT).
     * 
     * Authentication is automatically populated by JwtAuthenticationFilter.
     * If no valid JWT is provided, Spring Security blocks the request
     * before this method is even called.
     */
    @Operation(
        summary = "Get current user profile",
        description = "Returns the profile of the currently authenticated user."
    )
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getProfile(Authentication authentication) {
        UserResponseDto response = authService.getCurrentUser(authentication);
        return ResponseEntity.ok(ApiResponseDto.success("Profile retrieved", response));
    }
}