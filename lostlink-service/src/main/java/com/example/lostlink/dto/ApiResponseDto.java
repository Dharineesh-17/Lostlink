package com.example.lostlink.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================
 * ApiResponseDto — Generic Wrapper for All API Responses
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * A consistent response format across all endpoints:
 * {
 *   "success": true,
 *   "message": "Item created successfully",
 *   "data": { ... },
 *   "timestamp": "2024-01-15T10:30:00"
 * }
 * 
 * Benefits:
 * - Frontend knows exactly how to parse every response
 * - Error responses follow the same structure
 * - Timestamp helps with debugging and caching
 * 
 * @JsonInclude(NON_NULL) — Omits null fields from JSON output.
 *   When "data" is null (e.g., delete operations), it won't appear
 *   in the response JSON, keeping the output clean.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    /** Factory method for success responses with data */
    public static <T> ApiResponseDto<T> success(String message, T data) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /** Factory method for success responses without data */
    public static <T> ApiResponseDto<T> success(String message) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /** Factory method for error responses */
    public static <T> ApiResponseDto<T> error(String message) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}