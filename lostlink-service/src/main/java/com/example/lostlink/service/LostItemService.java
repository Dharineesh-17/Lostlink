package com.example.lostlink.service;

import com.example.lostlink.dto.LostItemRequestDto;
import com.example.lostlink.dto.LostItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ============================================================
 * LostItemService — Lost/Found Item Business Logic Interface
 * ============================================================
 */
public interface LostItemService {

    LostItemResponseDto createItem(LostItemRequestDto requestDto, String email);

    LostItemResponseDto getItemById(Long id);

    Page<LostItemResponseDto> getAllItems(Pageable pageable);

    Page<LostItemResponseDto> searchItems(String keyword, String status, Pageable pageable);

    Page<LostItemResponseDto> getMyItems(String email, Pageable pageable);

    LostItemResponseDto updateItem(Long id, LostItemRequestDto requestDto, String email);

    void deleteItem(Long id, String email);

    LostItemResponseDto updateItemStatus(Long id, String status, String email);
}