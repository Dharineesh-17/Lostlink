package com.example.lostlink.repository;

import com.example.lostlink.entity.Claim;
import com.example.lostlink.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================
 * ClaimRepository — Spring Data JPA Repository for Claim Entity
 * ============================================================
 *
 * WHY THIS EXISTS:
 * Manages claim data access including filtering by student,
 * item, and status. Used in both student dashboard (my claims)
 * and security dashboard (pending claims to review).
 *
 * KEY METHODS:
 * - findByStudentId() — Student's own claim history
 * - findByItemId() — All claims on a specific item
 * - findByStudentIdAndItemId() — Duplicate claim prevention
 * - existsByStudentIdAndItemIdAndStatus() — Efficient existence check
 * - findByStatus() — Security staff pending-claims queue
 * - countByStatus() — Dashboard statistics
 * ============================================================
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    /**
     * Finds all claims submitted by a specific student.
     * Used in the student dashboard to display "My Claims".
     *
     * @param studentId the ID of the student who submitted the claims
     * @return list of claims submitted by the given student
     */
    List<Claim> findByStudentId(Long studentId);

    /**
     * Finds all claims for a specific item.
     * Used when displaying item details (shows who has claimed it).
     *
     * @param itemId the ID of the item that was claimed
     * @return list of claims associated with the given item
     */
    List<Claim> findByItemId(Long itemId);

    /**
     * Finds a claim by student and item — ensures a student
     * can't claim the same item twice.
     *
     * @param studentId the ID of the student
     * @param itemId the ID of the item
     * @return Optional containing the claim if one exists for this
     *         student+item combination, empty otherwise
     */
    Optional<Claim> findByStudentIdAndItemId(Long studentId, Long itemId);

    /**
     * Checks if a pending claim exists for a student+item combo.
     * More efficient than findBy...().isPresent() because it
     * generates a COUNT query instead of fetching the full row.
     *
     * @param studentId the ID of the student
     * @param itemId the ID of the item
     * @param status the claim status to check for (typically PENDING)
     * @return true if a claim with the given status exists for this
     *         student+item combination
     */
    boolean existsByStudentIdAndItemIdAndStatus(Long studentId, Long itemId, ClaimStatus status);

    /**
     * Finds all claims with a specific status.
     * Used by security staff to find PENDING claims that need review.
     *
     * @param status the claim status to filter by
     * @return list of claims with the given status
     */
    List<Claim> findByStatus(ClaimStatus status);

    /**
     * Counts claims by status for dashboard statistics.
     *
     * @param status the claim status to count
     * @return number of claims with the given status
     */
    long countByStatus(ClaimStatus status);
}