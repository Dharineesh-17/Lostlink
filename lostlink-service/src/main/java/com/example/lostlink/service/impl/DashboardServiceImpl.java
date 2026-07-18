package com.example.lostlink.service.impl;

import com.example.lostlink.dto.DashboardStatsDto;
import com.example.lostlink.entity.User;
import com.example.lostlink.enums.ItemStatus;
import com.example.lostlink.enums.ClaimStatus;
import com.example.lostlink.enums.UserRole;
import com.example.lostlink.exception.ResourceNotFoundException;
import com.example.lostlink.repository.*;
import com.example.lostlink.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ============================================================
 * DashboardServiceImpl — Dashboard Statistics Implementation
 * ============================================================
 *
 * WHY THIS EXISTS:
 * Each user role sees different dashboard stats:
 * - ADMIN: full system overview (all users, all items, all claims)
 * - SECURITY: pending verifications and claims
 * - STUDENT: their own items and claims
 * ============================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final LostItemRepository itemRepository;
    private final ClaimRepository claimRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDto getAdminStats() {
        return DashboardStatsDto.builder()
                .totalUsers(userRepository.count())
                .totalLostItems(itemRepository.countByStatus(ItemStatus.LOST))
                .totalFoundItems(itemRepository.countByStatus(ItemStatus.FOUND))
                .totalPendingClaims(claimRepository.countByStatus(ClaimStatus.PENDING))
                .totalClaimedItems(itemRepository.countByStatus(ItemStatus.CLAIMED))
                .totalReturnedItems(itemRepository.countByStatus(ItemStatus.RETURNED))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDto getSecurityStats() {
        return DashboardStatsDto.builder()
                .totalPendingClaims(claimRepository.countByStatus(ClaimStatus.PENDING))
                .totalLostItems(itemRepository.countByStatus(ItemStatus.LOST))
                .totalFoundItems(itemRepository.countByStatus(ItemStatus.FOUND))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDto getStudentStats(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        var myItems = itemRepository.findByReportedById(user.getId());
        var myClaims = claimRepository.findByStudentId(user.getId());

        long myLostCount = myItems.stream().filter(i -> i.getStatus() == ItemStatus.LOST).count();
        long myFoundCount = myItems.stream().filter(i -> i.getStatus() == ItemStatus.FOUND).count();
        long myPendingClaims = myClaims.stream().filter(c -> c.getStatus() == ClaimStatus.PENDING).count();

        return DashboardStatsDto.builder()
                .totalLostItems(myLostCount)
                .totalFoundItems(myFoundCount)
                .totalPendingClaims(myPendingClaims)
                .build();
    }
}