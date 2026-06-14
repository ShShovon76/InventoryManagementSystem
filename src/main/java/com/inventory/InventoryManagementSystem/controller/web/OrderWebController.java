package com.inventory.InventoryManagementSystem.controller.web;

import com.inventory.InventoryManagementSystem.DTOs.request.OrderRequestDTO;
import com.inventory.InventoryManagementSystem.DTOs.response.OrderResponseDTO;
import com.inventory.InventoryManagementSystem.enums.OrderStatus;
import com.inventory.InventoryManagementSystem.service.OrderService;
import com.inventory.InventoryManagementSystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderWebController extends BaseWebController {

    private final OrderService orderService;
    private final ProductService productService;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("activePage", "orders");
        return "order/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("order", new OrderRequestDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("activePage", "orders");
        return "order/form";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("order") OrderRequestDTO request,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("orderStatuses", OrderStatus.values());
            model.addAttribute("activePage", "orders");
            return "order/form";
        }

        try {
            OrderResponseDTO response = orderService.createOrder(request);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Order #" + response.getId() + " created successfully! Total: $" + response.getTotalAmount());
            return "redirect:/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/orders/create";
        }
    }

    @GetMapping("/view/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        OrderResponseDTO order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("activePage", "orders");
        return "order/view";
    }

    @GetMapping("/status/{status}")
    public String filterByStatus(@PathVariable OrderStatus status, Model model) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        model.addAttribute("orders", orders);
        model.addAttribute("currentStatus", status);
        model.addAttribute("activePage", "orders");
        return "order/list";
    }

    @GetMapping("/customer/{customerName}")
    public String filterByCustomer(@PathVariable String customerName, Model model) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomer(customerName);
        model.addAttribute("orders", orders);
        model.addAttribute("currentCustomer", customerName);
        model.addAttribute("activePage", "orders");
        return "order/list";
    }

    @PostMapping("/update-status/{id}")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam OrderStatus status,
                                    RedirectAttributes redirectAttributes) {
        try {
            OrderResponseDTO order = orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Order #" + order.getId() + " status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order #" + id + " cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders";
    }
}