package com.example.lostlink.entity;

import com.example.lostlink.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================
 * Claim — JPA Entity for item claim requests
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * When a student finds their lost item (or believes they found it),
 * they submit a Claim. Security staff then reviews the claim —
 * verifying proof of ownership — and approves or rejects it.
 * 
 * WORKFLOW:
 * 1. Student creates a Claim (status: PENDING)
 * 2. Security staff reviews the claim
 * 3. If approved → Claim status = APPROVED, Item status = RETURNED
 * 4. If rejected → Claim status = REJECTED, Item status reverts
 * 
 * RELATIONSHIPS:
 * - ManyToOne → User: the student filing the claim
 * - ManyToOne → LostItem: the item being claimed
 * ============================================================
 */
@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_date", nullable = false)
    private LocalDateTime claimDate;

    @Column(length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ClaimStatus status = ClaimStatus.PENDING;

    /** The student who is claiming the item */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /** The item being claimed */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private LostItem item;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        claimDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}