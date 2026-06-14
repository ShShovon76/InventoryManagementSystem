package com.inventory.InventoryManagementSystem.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public abstract class BaseWebController {

    @ModelAttribute("currentPath")
    public String getCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("appName")
    public String getAppName() {
        return "Inventory Management System";
    }
}