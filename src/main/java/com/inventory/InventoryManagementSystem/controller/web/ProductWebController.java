package com.inventory.InventoryManagementSystem.controller.web;

import com.inventory.InventoryManagementSystem.DTOs.request.ProductRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.ProductResponseDTO;
import com.inventory.InventoryManagementSystem.service.CategoryService;
import com.inventory.InventoryManagementSystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductWebController extends BaseWebController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("activePage", "products");
        return "product/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductRequestDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("activePage", "products");
        return "product/form";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") ProductRequestDTO request,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("activePage", "products");
            return "product/form";
        }

        try {
            ProductResponseDTO response = productService.createProduct(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Product '" + response.getName() + "' created successfully!");
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductResponseDTO product = productService.getProductById(id);
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName(product.getName());
        request.setDescription(product.getDescription());
        request.setPrice(product.getPrice());
        request.setCategoryId(product.getCategory().getId());

        model.addAttribute("product", request);
        model.addAttribute("productId", id);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("activePage", "products");
        return "product/form";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") ProductRequestDTO request,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productId", id);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("activePage", "products");
            return "product/form";
        }

        try {
            ProductResponseDTO response = productService.updateProduct(id, request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Product '" + response.getName() + "' updated successfully!");
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        ProductResponseDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("activePage", "products");
        return "product/view";
    }
}