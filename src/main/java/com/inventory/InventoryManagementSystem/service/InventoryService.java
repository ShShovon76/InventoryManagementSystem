package com.inventory.InventoryManagementSystem.service;

import com.inventory.InventoryManagementSystem.DTOs.request.InventoryRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.InventoryResponseDTO;

import java.util.List;

public interface InventoryService {
    InventoryResponseDTO createOrUpdateInventory(InventoryRequestDTO request);
    InventoryResponseDTO getInventoryByProductId(Long productId);
    List<InventoryResponseDTO> getAllInventories();
    InventoryResponseDTO updateQuantity(Long productId, Integer quantity);
    void deleteInventory(Long id);
}