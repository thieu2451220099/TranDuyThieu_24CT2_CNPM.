package com.shop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "don_hang")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private User customer;

    @Column(name = "tong_tien", nullable=false)
    private double total;

    @Column(name = "phuong_thuc_thanh_toan", length = 30, nullable = false)
    private String paymentMethod = "COD";

    @Column(name = "trang_thai_thanh_toan", length = 30, nullable = false)
    private String paymentStatus = "UNPAID";

    @Column(name = "trang_thai", nullable=false, length=30)
    private String status = "PENDING";

    @Column(name = "ngay_tao", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "dia_chi_giao_hang", length=255)
    private String shippingAddress;

    public Order() {}
    public Long getId(){ return id; }
    public User getCustomer(){ return customer; }
    public void setCustomer(User customer){ this.customer=customer; }
    public double getTotal(){ return total; }
    public void setTotal(double total){ this.total=total; }
    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status=status; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public String getShippingAddress(){ return shippingAddress; }
    public void setShippingAddress(String shippingAddress){ this.shippingAddress=shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
