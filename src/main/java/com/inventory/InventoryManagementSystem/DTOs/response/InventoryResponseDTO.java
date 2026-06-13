package com.inventory.InventoryManagementSystem.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private LocalDateTime lastUpdated;
}