package com.inventory.InventoryManagementSystem.service.impl;




import com.inventory.InventoryManagementSystem.DTOs.request.InventoryRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.InventoryResponseDTO;
import com.inventory.InventoryManagementSystem.entity.Inventory;
import com.inventory.InventoryManagementSystem.entity.Product;
import com.inventory.InventoryManagementSystem.exception.ResourceNotFoundException;
import com.inventory.InventoryManagementSystem.repository.InventoryRepository;
import com.inventory.InventoryManagementSystem.repository.ProductRepository;
import com.inventory.InventoryManagementSystem.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryResponseDTO createOrUpdateInventory(InventoryRequestDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .orElse(new Inventory());

        inventory.setProduct(product);
        inventory.setQuantity(request.getQuantity());
        inventory.setLastUpdated(LocalDateTime.now());

        Inventory savedInventory = inventoryRepository.save(inventory);
        return mapToResponseDTO(savedInventory);
    }

    @Override
    public InventoryResponseDTO getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));
        return mapToResponseDTO(inventory);
    }

    @Override
    public List<InventoryResponseDTO> getAllInventories() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponseDTO updateQuantity(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));

        inventory.setQuantity(quantity);
        inventory.setLastUpdated(LocalDateTime.now());

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return mapToResponseDTO(updatedInventory);
    }

    @Override
    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    private InventoryResponseDTO mapToResponseDTO(Inventory inventory) {
        InventoryResponseDTO response = new InventoryResponseDTO();
        response.setId(inventory.getId());
        response.setProductId(inventory.getProduct().getId());
        response.setProductName(inventory.getProduct().getName());
        response.setQuantity(inventory.getQuantity());
        response.setLastUpdated(inventory.getLastUpdated());
        return response;
    }
}