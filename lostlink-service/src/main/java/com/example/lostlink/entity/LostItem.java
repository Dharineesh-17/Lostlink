package com.example.lostlink.entity;

import com.example.lostlink.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================
 * LostItem — JPA Entity for lost/found item reports
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * This is the core business entity. Every time a student reports
 * a lost or found item, a row is inserted into the "lost_items" table.
 * Security staff can update the status (e.g., mark as FOUND), and
 * students can search these records.
 * 
 * RELATIONSHIPS:
 * - ManyToOne → User: the student who reported this item
 * - OneToMany ← Claim: students who have claimed this item
 * 
 * KEY ANNOTATIONS:
 * @ManyToOne             — Many LostItems can belong to one User
 * @JoinColumn            — Specifies the foreign key column name
 * @OneToMany             — One LostItem can have many Claims
 * @OneToMany(mappedBy=...) — The owning side is Claim.item, so we
 *                           don't create a separate join column here
 * ============================================================
 */
@Entity
@Table(name = "lost_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LostItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 50)
    private String category;

    @Column(length = 200)
    private String location;

    @Column(name = "date_lost")
    private LocalDateTime dateLost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ItemStatus status = ItemStatus.LOST;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /** The user who reported this item */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}