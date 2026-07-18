package com.example.lostlink.enums;

/**
 * ============================================================
 * ClaimStatus — Enum for Claim Workflow States
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * When a student claims a found item, the claim goes through a
 * review workflow managed by security staff:
 *   PENDING → APPROVED / REJECTED
 * This enum represents those states so the service layer can
 * enforce valid state transitions.
 * ============================================================
 */
public enum ClaimStatus {
    /** Claim submitted; waiting for security staff review */
    PENDING,

    /** Security staff verified and approved the claim */
    APPROVED,

    /** Security staff rejected the claim (insufficient proof) */
    REJECTED
}