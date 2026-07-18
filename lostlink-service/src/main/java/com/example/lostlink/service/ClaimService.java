package com.example.lostlink.service;

import com.example.lostlink.dto.*;

import java.util.List;

/**
 * ============================================================
 * ClaimService — Claim Workflow Business Logic Interface
 * ============================================================
 */
public interface ClaimService {

    ClaimResponseDto createClaim(ClaimRequestDto requestDto, String email);

    List<ClaimResponseDto> getMyClaims(String email);

    List<ClaimResponseDto> getClaimsByItem(Long itemId);

    List<ClaimResponseDto> getPendingClaims();

    ClaimResponseDto updateClaimStatus(Long claimId, ClaimStatusUpdateDto statusDto, String email);
}