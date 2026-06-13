package com.inventory.InventoryManagementSystem.service;

import com.inventory.InventoryManagementSystem.DTOs.request.OrderRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.OrderResponseDTO;
import com.inventory.InventoryManagementSystem.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO request);
    OrderResponseDTO getOrderById(Long id);
    List<OrderResponseDTO> getAllOrders();
    List<OrderResponseDTO> getOrdersByStatus(OrderStatus status);
    List<OrderResponseDTO> getOrdersByCustomer(String customerName);
    OrderResponseDTO updateOrderStatus(Long id, OrderStatus status);
    void cancelOrder(Long id);
}