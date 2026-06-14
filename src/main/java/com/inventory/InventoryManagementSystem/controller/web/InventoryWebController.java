package com.inventory.InventoryManagementSystem.controller.web;

import com.inventory.InventoryManagementSystem.DTOs.request.InventoryRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.InventoryResponseDTO;
import com.inventory.InventoryManagementSystem.service.InventoryService;
import com.inventory.InventoryManagementSystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryWebController extends BaseWebController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    @GetMapping
    public String listInventory(Model model) {
        List<InventoryResponseDTO> inventories = inventoryService.getAllInventories();
        model.addAttribute("inventories", inventories);
        model.addAttribute("activePage", "inventory");
        return "inventory/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("inventory", new InventoryRequestDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("activePage", "inventory");
        return "inventory/form";
    }

    @PostMapping("/create")
    public String createOrUpdateInventory(@Valid @ModelAttribute("inventory") InventoryRequestDTO request,
                                          BindingResult result,
                                          RedirectAttributes redirectAttributes,
                                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("activePage", "inventory");
            return "inventory/form";
        }

        try {
            InventoryResponseDTO response = inventoryService.createOrUpdateInventory(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Inventory for '" + response.getProductName() + "' updated successfully!");
            return "redirect:/inventory";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/inventory/create";
        }
    }

    @GetMapping("/edit/{productId}")
    public String showEditForm(@PathVariable Long productId, Model model) {
        InventoryResponseDTO inventory = inventoryService.getInventoryByProductId(productId);
        InventoryRequestDTO request = new InventoryRequestDTO();
        request.setProductId(inventory.getProductId());
        request.setQuantity(inventory.getQuantity());

        model.addAttribute("inventory", request);
        model.addAttribute("productId", productId);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("activePage", "inventory");
        return "inventory/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteInventory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inventoryService.deleteInventory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Inventory record deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/inventory";
    }

    @GetMapping("/low-stock")
    public String lowStockInventory(Model model) {
        List<InventoryResponseDTO> lowStock = inventoryService.getAllInventories().stream()
                .filter(inv -> inv.getQuantity() < 10)
                .toList();
        model.addAttribute("inventories", lowStock);
        model.addAttribute("lowStockAlert", true);
        model.addAttribute("activePage", "inventory");
        return "inventory/list";
    }
}