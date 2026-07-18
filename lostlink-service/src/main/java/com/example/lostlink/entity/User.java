package com.example.lostlink.entity;

import com.example.lostlink.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ============================================================
 * User — JPA Entity representing a system user
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * The User entity maps to the "users" table in MySQL. It stores
 * authentication credentials (email, password) and authorization
 * data (role). Every lost item report, claim, and notification
 * references a User through foreign key relationships.
 * 
 * KEY ANNOTATIONS:
 * @Entity              — Marks this class as a JPA entity (maps to a DB table)
 * @Table               — Specifies the table name (best practice: explicit naming)
 * @Getter/@Setter      — Lombok: auto-generates getter/setter methods
 * @NoArgsConstructor   — Lombok: generates a no-args constructor (required by JPA)
 * @AllArgsConstructor — Lombok: generates a constructor with all fields
 * @Builder             — Lombok: generates a builder pattern for clean object creation
 * @Id                 — Marks this field as the primary key
 * @GeneratedValue      — Auto-generates the ID (IDENTITY = auto-increment in MySQL)
 * @Enumerated          — Stores the enum as a STRING in the DB (not ordinal number)
 * @Column             — Configures column properties (nullable, unique, length)
 * @PrePersist          — JPA callback: runs before INSERT to set createdAt
 * @PreUpdate           — JPA callback: runs before UPDATE to set updatedAt
 * ============================================================
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * JPA lifecycle callback — runs automatically before the entity
     * is first persisted (INSERT) to the database.
     * Sets the creation timestamp.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * JPA lifecycle callback — runs automatically before the entity
     * is updated (UPDATE) in the database.
     * Updates the modification timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}