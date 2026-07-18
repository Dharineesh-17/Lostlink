package com.example.lostlink.exception;

import com.example.lostlink.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================
 * GlobalExceptionHandler — Centralized Exception Handling
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Without this, Spring would return a raw stack trace to the client
 * when an exception occurs. That's:
 * 1. Unprofessional (looks broken)
 * 2. A security risk (exposes internal code structure)
 * 3. Hard for the frontend to parse
 * 
 * This class uses @RestControllerAdvice which is a combination of
 * @ControllerAdvice + @ResponseBody. It intercepts ALL exceptions
 * thrown from ANY controller and converts them to clean JSON responses.
 * 
 * @Hidden — Excludes this class from Swagger documentation
 *   (error handlers are not API endpoints)
 * @Slf4j — Lombok: generates logger
 * 
 * EXCEPTION HANDLING ORDER:
 * 1. Most specific exceptions first (ResourceNotFoundException)
 * 2. Validation errors (MethodArgumentNotValidException)
 * 3. Generic fallback (Exception.class)
 * ============================================================
 */
@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {

    /**
     * Handles "resource not found" errors → 404 Not Found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDto.error(ex.getMessage()));
    }

    /**
     * Handles "unauthorized access" errors → 403 Forbidden.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleUnauthorized(UnauthorizedException ex) {
        log.error("Unauthorized access: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponseDto.error(ex.getMessage()));
    }

    /**
     * Handles "bad request" (business logic violations) → 400 Bad Request.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBadRequest(BadRequestException ex) {
        log.error("Bad request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(ex.getMessage()));
    }

    /**
     * Handles "illegal argument" (e.g., passwords don't match) → 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(ex.getMessage()));
    }

    /**
     * Handles JWT / bad credentials errors → 401 Unauthorized.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBadCredentials(BadCredentialsException ex) {
        log.error("Bad credentials: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDto.error("Invalid email or password"));
    }

    /**
     * Handles Bean Validation errors → 400 Bad Request with field-level details.
     * 
     * When @Valid fails on a @RequestBody, Spring throws MethodArgumentNotValidException.
     * This method extracts each field error and returns a map like:
     * {
     *   "email": "Invalid email format",
     *   "password": "Password must contain at least one uppercase letter"
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Validation errors: {}", errors);

        ApiResponseDto<Map<String, String>> response = ApiResponseDto.<Map<String, String>>builder()
                .success(false)
                .message("Validation failed")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * CATCH-ALL handler for any unhandled exceptions → 500 Internal Server Error.
     * This ensures the client always gets a clean response, never a raw stack trace.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDto.error("An unexpected error occurred. Please try again later."));
    }
}