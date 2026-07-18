package com.example.lostlink.exception;

/**
 * ============================================================
 * UnauthorizedException — Thrown When User Lacks Permission
 * ============================================================
 * 
 * Used when a user tries to access or modify a resource they
 * don't own (e.g., deleting another user's item report).
 * ============================================================
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}