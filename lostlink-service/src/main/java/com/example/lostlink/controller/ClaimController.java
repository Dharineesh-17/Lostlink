package com.example.lostlink.controller;

import com.example.lostlink.dto.*;
import com.example.lostlink.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================================================
 * ClaimController — Claim Workflow REST Endpoints
 * ============================================================
 */
@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
@Tag(name = "Claims", description = "Item claim workflow endpoints")
public class ClaimController {

    private final ClaimService claimService;

    @Operation(summary = "File a claim on an item")
    @PostMapping
    public ResponseEntity<ApiResponseDto<ClaimResponseDto>> createClaim(
            @Valid @RequestBody ClaimRequestDto requestDto,
            Authentication authentication) {
        ClaimResponseDto response = claimService.createClaim(requestDto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Claim submitted successfully", response));
    }

    @Operation(summary = "Get my claims")
    @GetMapping("/my-claims")
    public ResponseEntity<ApiResponseDto<List<ClaimResponseDto>>> getMyClaims(
            Authentication authentication) {
        List<ClaimResponseDto> response = claimService.getMyClaims(authentication.getName());
        return ResponseEntity.ok(ApiResponseDto.success("My claims retrieved", response));
    }

    @Operation(summary = "Get claims for a specific item")
    @GetMapping("/item/{itemId}")
    public ResponseEntity<ApiResponseDto<List<ClaimResponseDto>>> getClaimsByItem(
            @PathVariable Long itemId) {
        List<ClaimResponseDto> response = claimService.getClaimsByItem(itemId);
        return ResponseEntity.ok(ApiResponseDto.success("Item claims retrieved", response));
    }

    @Operation(summary = "Get all pending claims (SECURITY/ADMIN only)")
    @PreAuthorize("hasAnyRole('SECURITY', 'ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponseDto<List<ClaimResponseDto>>> getPendingClaims() {
        List<ClaimResponseDto> response = claimService.getPendingClaims();
        return ResponseEntity.ok(ApiResponseDto.success("Pending claims retrieved", response));
    }

    @Operation(summary = "Approve or reject a claim (SECURITY/ADMIN only)")
    @PreAuthorize("hasAnyRole('SECURITY', 'ADMIN')")
    @PutMapping("/review/{claimId}")
    public ResponseEntity<ApiResponseDto<ClaimResponseDto>> updateClaimStatus(
            @PathVariable Long claimId,
            @Valid @RequestBody ClaimStatusUpdateDto statusDto) {
        ClaimResponseDto response = claimService.updateClaimStatus(claimId, statusDto, null);
        return ResponseEntity.ok(ApiResponseDto.success("Claim status updated", response));
    }
}