package com.shop.controller;

import com.shop.model.Order;
import com.shop.model.Product;
import com.shop.model.User;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.ProductRepository;
import com.shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductRepository products;
    private final OrderRepository orders;
    private final OrderItemRepository items;
    private final OrderService orderService;

    public AdminController(ProductRepository products, OrderRepository orders, OrderItemRepository items, OrderService orderService) {
        this.products = products;
        this.orders = orders;
        this.items = items;
        this.orderService = orderService;
    }

    private boolean admin(HttpSession session) {
        Object value = session.getAttribute("user");
        return value instanceof User && "ADMIN".equals(((User) value).getRole());
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (!admin(session)) return "redirect:/login";
        model.addAttribute("products", products.findAll());
        model.addAttribute("orders", orders.findAllByOrderByCreatedAtDesc());
        model.addAttribute("pendingCount", orders.countByStatus("PENDING"));
        model.addAttribute("confirmedCount", orders.countByStatus("CONFIRMED"));
        model.addAttribute("shippingCount", orders.countByStatus("SHIPPING"));
        model.addAttribute("completedCount", orders.countByStatus("COMPLETED"));
        model.addAttribute("cancelledCount", orders.countByStatus("CANCELLED"));
        Object error = session.getAttribute("adminError");
        if (error != null) { model.addAttribute("adminError", error); session.removeAttribute("adminError"); }
        return "admin";
    }

    @PostMapping("/products/save")
    public String save(Product product, HttpSession session) {
        if (!admin(session)) return "redirect:/login";
        products.save(product);
        return "redirect:/admin";
    }

    @PostMapping("/products/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (!admin(session)) return "redirect:/login";
        products.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/orders/{id}/status")
    public String status(@PathVariable Long id, @RequestParam String status, HttpSession session) {
        if (!admin(session)) return "redirect:/login";
        try {
            orderService.updateStatus(id, status);
        } catch (IllegalArgumentException ex) {
            session.setAttribute("adminError", ex.getMessage());
        }
        return "redirect:/admin";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        if (!admin(session)) return "redirect:/login";
        Order order = orders.findById(id).orElse(null);
        if (order == null) return "redirect:/admin";
        model.addAttribute("order", order);
        model.addAttribute("items", items.findByOrderId(id));
        return "admin-order-detail";
    }
}
