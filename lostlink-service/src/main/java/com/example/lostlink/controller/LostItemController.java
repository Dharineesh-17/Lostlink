package com.example.lostlink.controller;

import com.example.lostlink.dto.*;
import com.example.lostlink.enums.ItemStatus;
import com.example.lostlink.service.LostItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================
 * LostItemController — Lost/Found Item REST Endpoints
 * ============================================================
 * 
 * Provides CRUD operations for lost and found items plus search.
 * Most endpoints require authentication (JWT).
 * The search endpoint is public (no JWT needed).
 * ============================================================
 */
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Lost & Found Items", description = "CRUD and search operations for lost/found items")
public class LostItemController {

    private final LostItemService itemService;

    @Operation(summary = "Report a lost or found item")
    @PostMapping
    public ResponseEntity<ApiResponseDto<LostItemResponseDto>> createItem(
            @Valid @RequestBody LostItemRequestDto requestDto,
            Authentication authentication) {
        LostItemResponseDto response = itemService.createItem(requestDto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Item reported successfully", response));
    }

    @Operation(summary = "Get item by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<LostItemResponseDto>> getItemById(
            @Parameter(description = "Item ID") @PathVariable Long id) {
        LostItemResponseDto response = itemService.getItemById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Item retrieved", response));
    }

    @Operation(summary = "Get all items with pagination")
    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<LostItemResponseDto>>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LostItemResponseDto> response = itemService.getAllItems(pageable);
        return ResponseEntity.ok(ApiResponseDto.success("Items retrieved", response));
    }

    @Operation(summary = "Search items by keyword and/or status")
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<LostItemResponseDto>>> searchItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LostItemResponseDto> response = itemService.searchItems(keyword, status, pageable);
        return ResponseEntity.ok(ApiResponseDto.success("Search results", response));
    }

    @Operation(summary = "Get current user's reported items")
    @GetMapping("/my-items")
    public ResponseEntity<ApiResponseDto<Page<LostItemResponseDto>>> getMyItems(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LostItemResponseDto> response = itemService.getMyItems(authentication.getName(), pageable);
        return ResponseEntity.ok(ApiResponseDto.success("My items retrieved", response));
    }

    @Operation(summary = "Update an item report")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<LostItemResponseDto>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody LostItemRequestDto requestDto,
            Authentication authentication) {
        LostItemResponseDto response = itemService.updateItem(id, requestDto, authentication.getName());
        return ResponseEntity.ok(ApiResponseDto.success("Item updated", response));
    }

    @Operation(summary = "Delete an item report")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteItem(
            @PathVariable Long id,
            Authentication authentication) {
        itemService.deleteItem(id, authentication.getName());
        return ResponseEntity.ok(ApiResponseDto.success("Item deleted"));
    }

    @Operation(summary = "Update item status (SECURITY/ADMIN only)")
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponseDto<LostItemResponseDto>> updateItemStatus(
            @PathVariable Long id,
            @RequestBody ItemStatusUpdateDto statusDto,
            Authentication authentication) {
        LostItemResponseDto response = itemService.updateItemStatus(id, statusDto.getStatus().name(), authentication.getName());
        return ResponseEntity.ok(ApiResponseDto.success("Item status updated", response));
    }
}