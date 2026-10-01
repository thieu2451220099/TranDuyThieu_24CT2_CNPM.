package com.shop.controller;

import com.shop.model.Order;
import com.shop.model.User;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import com.shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class OrderController {
    private final OrderRepository orders;
    private final OrderItemRepository items;
    private final OrderService orderService;
    public OrderController(OrderRepository orders, OrderItemRepository items, OrderService orderService){this.orders=orders;this.items=items;this.orderService=orderService;}

    @SuppressWarnings("unchecked")
    @PostMapping("/checkout")
    public String checkout(@RequestParam String address,HttpSession session, @RequestParam(defaultValue = "COD") String paymentMethod){
        User user=(User)session.getAttribute("user"); if(user==null)return "redirect:/login";
        Map<String,Integer> cart=(Map<String,Integer>)session.getAttribute("cart");
        try{orderService.createOrder(user,address,cart,paymentMethod);session.removeAttribute("cart");return "redirect:/orders";}
        catch(IllegalArgumentException ex){session.setAttribute("checkoutError",ex.getMessage());return "redirect:/cart";}
    }

    @GetMapping("/orders")
    public String orders(HttpSession session,Model model){
        User user=(User)session.getAttribute("user"); if(user==null)return "redirect:/login";
        model.addAttribute("orders",orders.findByCustomerOrderByCreatedAtDesc(user)); return "orders";
    }

    @GetMapping("/orders/{id}")
    public String detail(@PathVariable Long id,HttpSession session,Model model){
        User user=(User)session.getAttribute("user"); if(user==null)return "redirect:/login";
        Order order=orders.findById(id).orElse(null);
        if(order==null||order.getCustomer()==null||!order.getCustomer().getId().equals(user.getId()))return "redirect:/orders";
        model.addAttribute("order",order);model.addAttribute("items",items.findByOrderId(id));
        Object error=session.getAttribute("orderError");if(error!=null){model.addAttribute("orderError",error);session.removeAttribute("orderError");}
        return "order-detail";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancel(@PathVariable Long id,HttpSession session){
        User user=(User)session.getAttribute("user");if(user==null)return "redirect:/login";
        try{orderService.cancelByCustomer(id,user);return "redirect:/orders/"+id;}
        catch(IllegalArgumentException ex){session.setAttribute("orderError",ex.getMessage());return "redirect:/orders/"+id;}
    }
}
