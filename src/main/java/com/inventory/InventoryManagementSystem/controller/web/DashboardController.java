package com.inventory.InventoryManagementSystem.controller.web;

import com.inventory.InventoryManagementSystem.repository.CategoryRepository;
import com.inventory.InventoryManagementSystem.repository.InventoryRepository;
import com.inventory.InventoryManagementSystem.repository.OrderRepository;
import com.inventory.InventoryManagementSystem.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController extends BaseWebController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        // Get statistics
        long totalCategories = categoryRepository.count();
        long totalProducts = productRepository.count();
        long totalInventory = inventoryRepository.count();
        long totalOrders = orderRepository.count();

        // Get low stock items (quantity < 10)
        var lowStockItems = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getQuantity() < 10)
                .count();

        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalInventory", totalInventory);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("lowStockItems", lowStockItems);
        model.addAttribute("activePage", "dashboard");

        return "dashboard";
    }
}
