package com.shop.repository;

import com.shop.model.Order;
import com.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);
    List<Order> findAllByOrderByCreatedAtDesc();
    long countByStatus(String status);
}
