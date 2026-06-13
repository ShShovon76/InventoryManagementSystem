package com.inventory.InventoryManagementSystem.controller;


import com.inventory.InventoryManagementSystem.DTOs.request.InventoryRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.InventoryResponseDTO;
import com.inventory.InventoryManagementSystem.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createOrUpdateInventory(@Valid @RequestBody InventoryRequestDTO request) {
        InventoryResponseDTO response = inventoryService.createOrUpdateInventory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponseDTO> getInventoryByProductId(@PathVariable Long productId) {
        InventoryResponseDTO response = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventories() {
        List<InventoryResponseDTO> responses = inventoryService.getAllInventories();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/product/{productId}/quantity")
    public ResponseEntity<InventoryResponseDTO> updateQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        InventoryResponseDTO response = inventoryService.updateQuantity(productId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}