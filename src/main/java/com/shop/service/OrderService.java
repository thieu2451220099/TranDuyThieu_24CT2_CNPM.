package com.shop.service;

import com.shop.controller.CartController;
import com.shop.model.Order;
import com.shop.model.OrderItem;
import com.shop.model.Product;
import com.shop.model.User;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final ProductRepository products;
    private final OrderRepository orders;
    private final OrderItemRepository items;

    public OrderService(ProductRepository products, OrderRepository orders, OrderItemRepository items) {
        this.products=products; this.orders=orders; this.items=items;
    }

    @Transactional
    public Order createOrder(User user, String address, Map<String,Integer> cart, String paymentMethod) {
        if (user == null) throw new IllegalArgumentException("Bạn cần đăng nhập để đặt hàng.");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Địa chỉ nhận hàng không được để trống.");
        if (cart == null || cart.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống.");
        if (!"COD".equals(paymentMethod) && !"BANK_TRANSFER".equals(paymentMethod)) paymentMethod = "COD";

        List<CartLine> lines = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : cart.entrySet()) {
            String[] parts = CartController.parseKey(entry.getKey());
            Long id = parseId(parts[0]);
            int requested = entry.getValue() == null ? 0 : entry.getValue();
            if (id == null || requested <= 0) continue;
            Product product = products.findById(id).orElse(null);
            if (product == null) continue;
            if (product.getQuantity() < requested) {
                throw new IllegalArgumentException("Sản phẩm \""+product.getName()+"\" chỉ còn "+product.getQuantity()+" sản phẩm.");
            }
            lines.add(new CartLine(product, requested, parts[1], parts[2]));
        }
        if (lines.isEmpty()) throw new IllegalArgumentException("Không có sản phẩm hợp lệ trong giỏ hàng.");

        Order order = new Order();
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("UNPAID");
        order.setCustomer(user);
        order.setShippingAddress(address.trim());
        order.setStatus("PENDING");
        double total=0;
        for (CartLine line : lines) total += line.product.getPrice()*line.quantity;
        order.setTotal(total);
        order = orders.save(order);

        for (CartLine line : lines) {
            line.product.setQuantity(line.product.getQuantity()-line.quantity);
            products.save(line.product);
            OrderItem item = new OrderItem();
            item.setOrder(order); item.setProduct(line.product); item.setQuantity(line.quantity);
            item.setPrice(line.product.getPrice()); item.setSize(line.size); item.setColor(line.color);
            items.save(item);
        }
        return order;
    }

    @Transactional
    public void updateStatus(Long id, String newStatus) {
        if (!List.of("PENDING","CONFIRMED","SHIPPING","COMPLETED","CANCELLED").contains(newStatus))
            throw new IllegalArgumentException("Trạng thái đơn hàng không hợp lệ.");
        Order order=orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng."));
        String old=order.getStatus();
        if ("CANCELLED".equals(old) && !"CANCELLED".equals(newStatus)) throw new IllegalArgumentException("Đơn đã hủy không thể mở lại.");
        if ("COMPLETED".equals(old) && !"COMPLETED".equals(newStatus)) throw new IllegalArgumentException("Đơn đã hoàn thành không thể đổi lại trạng thái.");
        if (!"CANCELLED".equals(old) && "CANCELLED".equals(newStatus)) restoreStock(order);
        order.setStatus(newStatus); orders.save(order);
    }

    @Transactional
    public void cancelByCustomer(Long id, User user) {
        Order order=orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng."));
        if (order.getCustomer()==null || !order.getCustomer().getId().equals(user.getId())) throw new IllegalArgumentException("Bạn không có quyền hủy đơn này.");
        if (!("PENDING".equals(order.getStatus()) || "CONFIRMED".equals(order.getStatus()))) throw new IllegalArgumentException("Chỉ có thể hủy đơn đang chờ xác nhận hoặc đã xác nhận.");
        restoreStock(order); order.setStatus("CANCELLED"); orders.save(order);
    }

    private void restoreStock(Order order) {
        for (OrderItem item : items.findByOrderId(order.getId())) {
            Product p=item.getProduct(); p.setQuantity(p.getQuantity()+item.getQuantity()); products.save(p);
        }
    }
    private Long parseId(String s){try{return Long.valueOf(s);}catch(Exception e){return null;}}
    private record CartLine(Product product,int quantity,String size,String color){}
}
