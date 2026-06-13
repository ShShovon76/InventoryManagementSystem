package com.inventory.InventoryManagementSystem.service;

import com.inventory.InventoryManagementSystem.DTOs.request.ProductRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO request);
    ProductResponseDTO getProductById(Long id);
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getProductsByCategory(Long categoryId);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO request);
    void deleteProduct(Long id);
}
