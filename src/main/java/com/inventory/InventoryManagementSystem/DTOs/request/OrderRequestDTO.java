package com.inventory.InventoryManagementSystem.DTOs.request;


import com.inventory.InventoryManagementSystem.enums.OrderStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;

    @NotEmpty(message = "At least one product is required")
    private List<@Positive(message = "Product ID must be positive") Long> productIds;
}