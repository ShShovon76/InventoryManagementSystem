package com.inventory.InventoryManagementSystem.DTOs.response;


import com.inventory.InventoryManagementSystem.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private String customerName;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<ProductResponseDTO> products;
}