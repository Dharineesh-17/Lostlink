package com.example.lostlink.service.impl;

import com.example.lostlink.dto.*;
import com.example.lostlink.entity.Claim;
import com.example.lostlink.entity.LostItem;
import com.example.lostlink.entity.Notification;
import com.example.lostlink.entity.User;
import com.example.lostlink.enums.ClaimStatus;
import com.example.lostlink.enums.ItemStatus;
import com.example.lostlink.exception.*;
import com.example.lostlink.repository.ClaimRepository;
import com.example.lostlink.repository.LostItemRepository;
import com.example.lostlink.repository.NotificationRepository;
import com.example.lostlink.repository.UserRepository;
import com.example.lostlink.service.ClaimService;
import com.example.lostlink.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================
 * ClaimServiceImpl — Claim Workflow Implementation
 * ============================================================
 *
 * WHY THIS EXISTS:
 * The claim workflow is the most complex business process in
 * LostLink. It enforces rules like:
 * - A student can't claim their own reported item
 * - A student can't claim the same item twice
 * - Only security/admin can approve/reject claims
 * - Approving a claim updates the item status to RETURNED
 * ============================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final LostItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public ClaimResponseDto createClaim(ClaimRequestDto requestDto, String email) {
        log.info("User {} creating claim for item {}", email, requestDto.getItemId());

        User student = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        LostItem item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("LostItem", "id", requestDto.getItemId()));

        // Business rule: can't claim your own item
        if (item.getReportedBy().getId().equals(student.getId())) {
            throw new BadRequestException("You cannot claim your own reported item");
        }

        // Business rule: can't claim already returned items
        if (item.getStatus() == ItemStatus.RETURNED) {
            throw new BadRequestException("This item has already been returned");
        }

        // Business rule: no duplicate pending claims
        if (claimRepository.existsByStudentIdAndItemIdAndStatus(
                student.getId(), item.getId(), ClaimStatus.PENDING)) {
            throw new BadRequestException("You already have a pending claim for this item");
        }

        Claim claim = Claim.builder()
                .reason(requestDto.getReason())
                .status(ClaimStatus.PENDING)
                .student(student)
                .item(item)
                .build();

        Claim savedClaim = claimRepository.save(claim);

        // Notify the item reporter
        notificationRepository.save(
                Notification.builder()
                        .message("A new claim has been filed on your item: " + item.getTitle())
                        .type("CLAIM")
                        .user(item.getReportedBy())
                        .build()
        );

        log.info("Claim created with ID: {}", savedClaim.getId());
        return mapToResponseDto(savedClaim);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimResponseDto> getMyClaims(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return claimRepository.findByStudentId(user.getId()).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimResponseDto> getClaimsByItem(Long itemId) {
        return claimRepository.findByItemId(itemId).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimResponseDto> getPendingClaims() {
        return claimRepository.findByStatus(ClaimStatus.PENDING).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public ClaimResponseDto updateClaimStatus(Long claimId, ClaimStatusUpdateDto statusDto, String email) {
        log.info("Updating claim {} to status {}", claimId, statusDto.getStatus());

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new BadRequestException("This claim has already been processed");
        }

        claim.setStatus(statusDto.getStatus());

        // If approved, update the item status
        if (statusDto.getStatus() == ClaimStatus.APPROVED) {
            claim.getItem().setStatus(ItemStatus.RETURNED);

            // Notify the claimer
            notificationRepository.save(
                    Notification.builder()
                            .message("Your claim on '" + claim.getItem().getTitle() + "' has been APPROVED!")
                            .type("APPROVED")
                            .user(claim.getStudent())
                            .build()
            );
        } else if (statusDto.getStatus() == ClaimStatus.REJECTED) {
            // Notify the claimer
            notificationRepository.save(
                    Notification.builder()
                            .message("Your claim on '" + claim.getItem().getTitle() + "' has been REJECTED. " +
                                    (statusDto.getRemark() != null ? "Reason: " + statusDto.getRemark() : ""))
                            .type("REJECTED")
                            .user(claim.getStudent())
                            .build()
            );
        }

        Claim updatedClaim = claimRepository.save(claim);
        log.info("Claim {} updated to {}", claimId, statusDto.getStatus());
        return mapToResponseDto(updatedClaim);
    }

    private ClaimResponseDto mapToResponseDto(Claim claim) {
        return ClaimResponseDto.builder()
                .id(claim.getId())
                .claimDate(claim.getClaimDate())
                .reason(claim.getReason())
                .status(claim.getStatus())
                .studentId(claim.getStudent().getId())
                .studentName(claim.getStudent().getName())
                .itemId(claim.getItem().getId())
                .itemTitle(claim.getItem().getTitle())
                .createdAt(claim.getCreatedAt())
                .build();
    }
}