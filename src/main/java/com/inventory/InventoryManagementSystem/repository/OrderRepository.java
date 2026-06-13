package com.inventory.InventoryManagementSystem.repository;

import com.inventory.InventoryManagementSystem.entity.Order;
import com.inventory.InventoryManagementSystem.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatus(OrderStatus status);
    List<Order> findByCustomerName(String customerName);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}