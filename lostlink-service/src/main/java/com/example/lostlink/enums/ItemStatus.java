package com.example.lostlink.enums;

/**
 * ============================================================
 * ItemStatus — Enum for Lost/Found Item Lifecycle
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Every lost/found item goes through a defined lifecycle:
 *   LOST → FOUND → CLAIMED → RETURNED
 * This enum models those states. Using an enum ensures that:
 * - Only valid transitions are represented in code
 * - The database column stores a consistent string value
 * - Business logic can switch/case on known states
 * ============================================================
 */
public enum ItemStatus {
    /** Item has been reported as lost by a student */
    LOST,

    /** Item has been found and reported (possibly by security or another student) */
    FOUND,

    /** A student has claimed this item; awaiting security verification */
    CLAIMED,

    /** Claim approved and item returned to the owner */
    RETURNED
}