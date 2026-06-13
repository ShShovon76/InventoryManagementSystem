package com.inventory.InventoryManagementSystem.service;

import com.inventory.InventoryManagementSystem.DTOs.request.CategoryRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO request);
    CategoryResponseDTO getCategoryById(Long id);
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request);
    void deleteCategory(Long id);
}