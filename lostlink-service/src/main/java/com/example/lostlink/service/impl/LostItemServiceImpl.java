package com.example.lostlink.service.impl;

import com.example.lostlink.dto.LostItemRequestDto;
import com.example.lostlink.dto.LostItemResponseDto;
import com.example.lostlink.entity.LostItem;
import com.example.lostlink.entity.User;
import com.example.lostlink.enums.ItemStatus;
import com.example.lostlink.exception.ResourceNotFoundException;
import com.example.lostlink.exception.UnauthorizedException;
import com.example.lostlink.repository.LostItemRepository;
import com.example.lostlink.repository.UserRepository;
import com.example.lostlink.service.LostItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ============================================================
 * LostItemServiceImpl — Lost/Found Item Service Implementation
 * ============================================================
 *
 * WHY THIS EXISTS:
 * Contains all business logic for managing lost and found items.
 * This is the "brain" of the item management feature — controllers
 * are thin wrappers that delegate to this service.
 * ============================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LostItemServiceImpl implements LostItemService {

    private final LostItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public LostItemResponseDto createItem(LostItemRequestDto requestDto, String email) {
        log.info("Creating item report for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        LostItem item = LostItem.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .category(requestDto.getCategory())
                .location(requestDto.getLocation())
                .dateLost(requestDto.getDateLost())
                .status(ItemStatus.LOST) // Default status
                .imageUrl(requestDto.getImageUrl())
                .reportedBy(user)
                .build();

        LostItem savedItem = itemRepository.save(item);
        log.info("Item created with ID: {}", savedItem.getId());

        return mapToResponseDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public LostItemResponseDto getItemById(Long id) {
        LostItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LostItem", "id", id));
        return mapToResponseDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LostItemResponseDto> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LostItemResponseDto> searchItems(String keyword, String status, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            if (status != null && !status.isBlank()) {
                return itemRepository.findByStatus(ItemStatus.valueOf(status.toUpperCase()), pageable)
                        .map(this::mapToResponseDto);
            }
            return itemRepository.findAll(pageable).map(this::mapToResponseDto);
        }

        if (status != null && !status.isBlank()) {
            return itemRepository.searchItemsByStatus(keyword, ItemStatus.valueOf(status.toUpperCase()), pageable)
                    .map(this::mapToResponseDto);
        }
        return itemRepository.searchItems(keyword, pageable).map(this::mapToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LostItemResponseDto> getMyItems(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        // Convert list to page manually
        var items = itemRepository.findByReportedById(user.getId());
        // For simplicity, return all items (pagination can be added with custom query)
        return new org.springframework.data.domain.PageImpl<>(
                items.stream().map(this::mapToResponseDto).toList(),
                pageable,
                items.size()
        );
    }

    @Override
    @Transactional
    public LostItemResponseDto updateItem(Long id, LostItemRequestDto requestDto, String email) {
        LostItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LostItem", "id", id));

        // Only the reporter or an admin can update
        if (!item.getReportedBy().getEmail().equals(email)) {
            throw new UnauthorizedException("You can only update your own reports");
        }

        item.setTitle(requestDto.getTitle());
        item.setDescription(requestDto.getDescription());
        item.setCategory(requestDto.getCategory());
        item.setLocation(requestDto.getLocation());
        item.setDateLost(requestDto.getDateLost());
        item.setImageUrl(requestDto.getImageUrl());

        // Don't change the status here — only security/admin can change status

        LostItem updatedItem = itemRepository.save(item);
        log.info("Item updated: {}", updatedItem.getId());
        return mapToResponseDto(updatedItem);
    }

    @Override
    @Transactional
    public void deleteItem(Long id, String email) {
        LostItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LostItem", "id", id));

        if (!item.getReportedBy().getEmail().equals(email)) {
            throw new UnauthorizedException("You can only delete your own reports");
        }

        itemRepository.delete(item);
        log.info("Item deleted: {}", id);
    }

    @Override
    @Transactional
    public LostItemResponseDto updateItemStatus(Long id, String status, String email) {
        LostItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LostItem", "id", id));

        item.setStatus(ItemStatus.valueOf(status.toUpperCase()));
        LostItem updated = itemRepository.save(item);
        log.info("Item {} status updated to {}", id, status);
        return mapToResponseDto(updated);
    }

    private LostItemResponseDto mapToResponseDto(LostItem item) {
        return LostItemResponseDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .location(item.getLocation())
                .dateLost(item.getDateLost())
                .status(item.getStatus())
                .imageUrl(item.getImageUrl())
                .reportedById(item.getReportedBy().getId())
                .reportedByName(item.getReportedBy().getName())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}