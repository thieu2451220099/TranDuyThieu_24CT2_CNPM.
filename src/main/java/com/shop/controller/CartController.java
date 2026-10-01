package com.shop.controller;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CartController {
    private final ProductRepository products;
    public CartController(ProductRepository products) { this.products = products; }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> cart(HttpSession session) {
        Object value = session.getAttribute("cart");
        if (value instanceof Map<?, ?> map) {
            Map<String, Integer> result = new LinkedHashMap<>();
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (e.getKey() != null && e.getValue() instanceof Number n) {
                    result.put(String.valueOf(e.getKey()), n.intValue());
                }
            }
            if (value != result) session.setAttribute("cart", result);
            return result;
        }
        Map<String, Integer> result = new LinkedHashMap<>();
        session.setAttribute("cart", result);
        return result;
    }

    public static String key(Long productId, String size, String color) {
        return productId + "|" + safe(size) + "|" + safe(color);
    }

    public static String safe(String value) { return value == null ? "" : value.trim(); }

    public static String[] parseKey(String key) {
        String[] parts = key.split("\\|", -1);
        if (parts.length != 3) return new String[]{parts.length > 0 ? parts[0] : "", "", ""};
        return parts;
    }

    @PostMapping("/cart/add/{id}")
    public String add(@PathVariable Long id,
                      @RequestParam(required=false) String size,
                      @RequestParam(required=false) String color,
                      @RequestParam(required=false, defaultValue="1") int quantity,
                      HttpSession session) {
        Product product = products.findById(id).orElse(null);
        if (product == null || product.getQuantity() <= 0) return "redirect:/";

        Map<String, Integer> cart = cart(session);
        String cartKey = key(id, size, color);
        int current = cart.getOrDefault(cartKey, 0);
        int addQty = Math.max(1, quantity);
        cart.put(cartKey, Math.min(product.getQuantity(), current + addQty));
        return "redirect:/cart";
    }

    @PostMapping("/cart/increase")
    public String increase(@RequestParam String key, HttpSession session) {
        Map<String, Integer> cart = cart(session);
        String[] parts = parseKey(key);
        Long id = parseId(parts[0]);
        Product product = id == null ? null : products.findById(id).orElse(null);
        if (product != null) cart.put(key, Math.min(product.getQuantity(), cart.getOrDefault(key, 0) + 1));
        return "redirect:/cart";
    }

    @PostMapping("/cart/decrease")
    public String decrease(@RequestParam String key, HttpSession session) {
        Map<String, Integer> cart = cart(session);
        int current = cart.getOrDefault(key, 0);
        if (current <= 1) cart.remove(key); else cart.put(key, current - 1);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam String key, HttpSession session) {
        cart(session).remove(key);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String view(HttpSession session, Model model) {
        Map<String, Integer> cart = cart(session);
        List<CartViewItem> items = new ArrayList<>();
        double total = 0;
        Iterator<Map.Entry<String, Integer>> iterator = cart.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String[] parts = parseKey(entry.getKey());
            Long id = parseId(parts[0]);
            Product product = id == null ? null : products.findById(id).orElse(null);
            if (product == null || product.getQuantity() <= 0) { iterator.remove(); continue; }
            int safeQty = Math.min(Math.max(entry.getValue(), 1), product.getQuantity());
            entry.setValue(safeQty);
            CartViewItem item = new CartViewItem(entry.getKey(), product, parts[1], parts[2], safeQty);
            items.add(item);
            total += item.getSubtotal();
        }
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        Object error = session.getAttribute("checkoutError");
        if (error != null) { model.addAttribute("checkoutError", error); session.removeAttribute("checkoutError"); }
        return "cart";
    }

    private Long parseId(String value) {
        try { return Long.valueOf(value); } catch (Exception e) { return null; }
    }

    public static class CartViewItem {
        private final String key;
        private final Product product;
        private final String size;
        private final String color;
        private final int quantity;
        public CartViewItem(String key, Product product, String size, String color, int quantity) {
            this.key=key; this.product=product; this.size=size; this.color=color; this.quantity=quantity;
        }
        public String getKey(){return key;}
        public Product getProduct(){return product;}
        public String getSize(){return size;}
        public String getColor(){return color;}
        public int getQuantity(){return quantity;}
        public double getSubtotal(){return product.getPrice()*quantity;}
    }
}
