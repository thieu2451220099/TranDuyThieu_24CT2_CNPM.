package com.shop.controller;

import com.shop.model.Product;
import com.shop.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    private final ProductRepository productRepository;
    public HomeController(ProductRepository productRepository){this.productRepository=productRepository;}

    @GetMapping("/")
    public String home(@RequestParam(required=false) String q, Model model) {
        model.addAttribute("products", q == null || q.isBlank()
                ? productRepository.findAll()
                : productRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(q, q));
        model.addAttribute("q", q == null ? "" : q);
        return "index";
    }

    @GetMapping("/product/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/";

        model.addAttribute("product", product);
        model.addAttribute("sizes", parseOptions(product.getAvailableSizes(), product.getCategory()));
        model.addAttribute("colors", parseOptions(product.getAvailableColors(), "Màu"));
        return "product-detail";
    }

    private List<String> parseOptions(String value, String fallbackType) {
        if (value != null && !value.isBlank()) {
            return Arrays.stream(value.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .toList();
        }
        if ("Giày".equalsIgnoreCase(fallbackType)) return List.of("38","39","40","41","42","43","44");
        if ("Màu".equalsIgnoreCase(fallbackType)) return List.of("Đen","Trắng","Xanh","Đỏ");
        if (fallbackType != null && (fallbackType.toLowerCase().contains("áo") || fallbackType.toLowerCase().contains("thời trang"))) return List.of("S","M","L","XL","XXL");
        return Collections.emptyList();
    }
}
