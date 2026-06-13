package com.inventory.InventoryManagementSystem.service.impl;


import com.inventory.InventoryManagementSystem.DTOs.request.ProductRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.CategoryResponseDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.InventoryResponseDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.ProductResponseDTO;
import com.inventory.InventoryManagementSystem.entity.Category;
import com.inventory.InventoryManagementSystem.entity.Product;
import com.inventory.InventoryManagementSystem.exception.ResourceNotFoundException;
import com.inventory.InventoryManagementSystem.repository.CategoryRepository;
import com.inventory.InventoryManagementSystem.repository.ProductRepository;
import com.inventory.InventoryManagementSystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapToResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponseDTO(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());

        // Map category
        if (product.getCategory() != null) {
            CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            categoryDTO.setDescription(product.getCategory().getDescription());
            response.setCategory(categoryDTO);
        }

        // Map inventory if exists
        if (product.getInventory() != null) {
            InventoryResponseDTO inventoryDTO = new InventoryResponseDTO();
            inventoryDTO.setId(product.getInventory().getId());
            inventoryDTO.setProductId(product.getId());
            inventoryDTO.setProductName(product.getName());
            inventoryDTO.setQuantity(product.getInventory().getQuantity());
            inventoryDTO.setLastUpdated(product.getInventory().getLastUpdated());
            response.setInventory(inventoryDTO);
        }

        return response;
    }
}