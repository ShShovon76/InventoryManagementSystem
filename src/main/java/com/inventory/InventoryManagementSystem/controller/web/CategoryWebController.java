package com.inventory.InventoryManagementSystem.controller.web;

import com.inventory.InventoryManagementSystem.DTOs.request.CategoryRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.CategoryResponseDTO;
import com.inventory.InventoryManagementSystem.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryWebController extends BaseWebController {

    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("activePage", "categories");
        return "category/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryRequestDTO());
        model.addAttribute("activePage", "categories");
        return "category/form";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("category") CategoryRequestDTO request,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("activePage", "categories");
            return "category/form";
        }

        try {
            CategoryResponseDTO response = categoryService.createCategory(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Category '" + response.getName() + "' created successfully!");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/categories/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName(category.getName());
        request.setDescription(category.getDescription());

        model.addAttribute("category", request);
        model.addAttribute("categoryId", id);
        model.addAttribute("activePage", "categories");
        return "category/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @Valid @ModelAttribute("category") CategoryRequestDTO request,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categoryId", id);
            model.addAttribute("activePage", "categories");
            return "category/form";
        }

        try {
            CategoryResponseDTO response = categoryService.updateCategory(id, request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Category '" + response.getName() + "' updated successfully!");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/categories/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/view/{id}")
    public String viewCategory(@PathVariable Long id, Model model) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("activePage", "categories");
        return "category/view";
    }
}