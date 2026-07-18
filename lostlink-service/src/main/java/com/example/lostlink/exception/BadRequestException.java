package com.example.lostlink.exception;

/**
 * ============================================================
 * BadRequestException — Thrown for Invalid Business Logic
 * ============================================================
 * 
 * Used for business rule violations that aren't caught by
 * Bean Validation (e.g., "you can't claim your own item").
 * ============================================================
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}