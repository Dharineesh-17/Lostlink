package com.example.lostlink.repository;

import com.example.lostlink.entity.User;
import com.example.lostlink.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================
 * UserRepository — Spring Data JPA Repository for User Entity
 * ============================================================
 *
 * WHY THIS EXISTS:
 * Spring Data JPA provides JpaRepository which auto-generates
 * CRUD operations (save, findById, findAll, delete, etc.) at
 * runtime using Hibernate. We only need to declare custom
 * query methods — Spring writes the SQL for us.
 *
 * @Repository — Marks this as a Data Access Object (DAO).
 *   Enables Spring's exception translation (converts DB-specific
 *   exceptions like MySQLIntegrityConstraintViolationException
 *   into Spring's DataAccessException hierarchy).
 *
 * KEY METHODS:
 * - findByEmail() — Used during login and registration to check
 *   if an email already exists
 * - existsByEmail() — Efficient boolean check (doesn't load the
 *   full entity, just checks existence)
 * ============================================================
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * Spring Data JPA automatically generates:
     *   SELECT * FROM users WHERE email = ?
     *
     * @param email the email to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given email exists.
     * More efficient than findByEmail().isPresent() because
     * it generates a COUNT query instead of fetching the full row.
     *
     * @param email the email to check
     * @return true if a user with this email exists
     */
    boolean existsByEmail(String email);

    /**
     * Counts users by their role.
     * Used in the admin dashboard for statistics.
     *
     * @param role the user role to count
     * @return number of users with the given role
     */
    long countByRole(UserRole role);
}