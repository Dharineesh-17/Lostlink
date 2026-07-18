package com.example.lostlink.repository;

import com.example.lostlink.entity.LostItem;
import com.example.lostlink.enums.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ============================================================
 * LostItemRepository — Spring Data JPA Repository for LostItem
 * ============================================================
 *
 * WHY THIS EXISTS:
 * Provides data access for lost/found items with custom search
 * queries. The search functionality uses @Query with LIKE clauses
 * for keyword-based searching across title, description, category,
 * and location fields.
 *
 * KEY METHODS:
 * - findByReportedById() — Get all items reported by a specific user
 * - findByStatus() — Filter items by their lifecycle status
 * - searchItems() — Full-text-like search across multiple columns
 * - countByStatus() — Dashboard statistics
 * ============================================================
 */
@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {

    /**
     * Finds all items reported by a specific user.
     * Used in the student dashboard to show "My Lost Items".
     *
     * @param reportedById the ID of the user who reported the items
     * @return list of items reported by the given user
     */
    List<LostItem> findByReportedById(Long reportedById);

    /**
     * Finds all items with a given status, with pagination.
     * @Pageable allows the frontend to request page 0, size 10, etc.
     *
     * @param status the item status to filter by
     * @param pageable pagination and sorting information
     * @return a page of items matching the given status
     */
    Page<LostItem> findByStatus(ItemStatus status, Pageable pageable);

    /**
     * Counts items by status.
     * Used for dashboard statistics (e.g., "Total Lost Items: 42").
     *
     * @param status the item status to count
     * @return number of items with the given status
     */
    long countByStatus(ItemStatus status);

    /**
     * Multi-field keyword search.
     * Uses @Query to write a custom JPQL (not native SQL).
     * The LOWER() function makes the search case-insensitive.
     * %:keyword% means "contains the keyword anywhere".
     *
     * Spring Data JPA binds the @Param("keyword") to the ?1 placeholder.
     *
     * @param keyword the search term to match against title, description,
     *                category, and location fields
     * @param pageable pagination and sorting information
     * @return a page of items matching the keyword in any searchable field
     */
    @Query("SELECT li FROM LostItem li WHERE " +
           "LOWER(li.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(li.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(li.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(li.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<LostItem> searchItems(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Search items filtered by a specific status.
     * Combines keyword search with status filtering.
     *
     * @param keyword the search term to match against title, description,
     *                category, and location fields
     * @param status the item status to filter by
     * @param pageable pagination and sorting information
     * @return a page of items matching the keyword and status
     */
    @Query("SELECT li FROM LostItem li WHERE li.status = :status AND (" +
           "LOWER(li.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(li.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(li.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(li.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<LostItem> searchItemsByStatus(@Param("keyword") String keyword,
                                        @Param("status") ItemStatus status,
                                        Pageable pageable);
}