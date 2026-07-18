package com.example.lostlink.enums;

/**
 * ============================================================
 * UserRole — Enum for User Authorization Levels
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * In a role-based access control (RBAC) system, each user is assigned
 * a role that determines which API endpoints they can access.
 * Spring Security uses this enum's name (e.g., "ROLE_ADMIN") to
 * perform authorization checks via @PreAuthorize and .hasRole().
 * 
 * Using an enum (instead of raw strings) provides:
 * - Type safety: you can't accidentally pass "SUPERADMIN"
 * - Single source of truth: change the role name here, it updates everywhere
 * - Database mapping: JPA can store this as a VARCHAR automatically
 * ============================================================
 */
public enum UserRole {
    /** Student — can report items, search, and file claims */
    STUDENT,

    /** Security Staff — can verify found items and approve/reject claims */
    SECURITY,

    /** Admin — full access to all management and reporting features */
    ADMIN
}