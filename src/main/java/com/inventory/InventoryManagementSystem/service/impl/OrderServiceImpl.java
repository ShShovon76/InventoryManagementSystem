package com.inventory.InventoryManagementSystem.service.impl;



import com.inventory.InventoryManagementSystem.DTOs.request.OrderRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.OrderResponseDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.ProductResponseDTO;
import com.inventory.InventoryManagementSystem.entity.Order;
import com.inventory.InventoryManagementSystem.entity.Product;
import com.inventory.InventoryManagementSystem.enums.OrderStatus;
import com.inventory.InventoryManagementSystem.exception.InsufficientStockException;
import com.inventory.InventoryManagementSystem.exception.ResourceNotFoundException;
import com.inventory.InventoryManagementSystem.repository.InventoryRepository;
import com.inventory.InventoryManagementSystem.repository.OrderRepository;
import com.inventory.InventoryManagementSystem.repository.ProductRepository;
import com.inventory.InventoryManagementSystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        // Fetch all products
        List<Product> products = productRepository.findAllById(request.getProductIds());

        if (products.size() != request.getProductIds().size()) {
            throw new ResourceNotFoundException("One or more products not found");
        }

        // Check stock availability
        for (Product product : products) {
            var inventory = inventoryRepository.findByProductId(product.getId())
                    .orElseThrow(() -> new InsufficientStockException("Product " + product.getName() + " not found in inventory"));

            if (inventory.getQuantity() <= 0) {
                throw new InsufficientStockException("Product " + product.getName() + " is out of stock");
            }
        }

        // Calculate total amount
        BigDecimal totalAmount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setOrderStatus(request.getOrderStatus());
        order.setTotalAmount(totalAmount);
        order.setProducts(products);

        // Deduct stock
        for (Product product : products) {
            var inventory = inventoryRepository.findByProductId(product.getId()).get();
            inventory.setQuantity(inventory.getQuantity() - 1);
            inventoryRepository.save(inventory);
        }

        Order savedOrder = orderRepository.save(order);
        return mapToResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomer(String customerName) {
        return orderRepository.findByCustomerName(customerName)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return mapToResponseDTO(updatedOrder);
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel delivered order");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setCustomerName(order.getCustomerName());
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderStatus(order.getOrderStatus());

        List<ProductResponseDTO> productDTOs = order.getProducts().stream()
                .map(this::mapProductToDTO)
                .collect(Collectors.toList());
        response.setProducts(productDTOs);

        return response;
    }

    private ProductResponseDTO mapProductToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        return dto;
    }
}