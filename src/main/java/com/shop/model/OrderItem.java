package com.shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chi_tiet_don_hang")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name = "don_hang_id", nullable = false)
    private Order order;

    @ManyToOne(optional=false)
    @JoinColumn(name = "san_pham_id", nullable = false)
    private Product product;

    @Column(name = "so_luong", nullable=false)
    private int quantity;

    @Column(name = "gia", nullable=false)
    private double price;

    @Column(name = "kich_thuoc", length=100)
    private String size;

    @Column(name = "mau_sac", length=100)
    private String color;

    public OrderItem() {}
    public Long getId(){ return id; }
    public Order getOrder(){ return order; }
    public void setOrder(Order order){ this.order=order; }
    public Product getProduct(){ return product; }
    public void setProduct(Product product){ this.product=product; }
    public int getQuantity(){ return quantity; }
    public void setQuantity(int quantity){ this.quantity=quantity; }
    public double getPrice(){ return price; }
    public void setPrice(double price){ this.price=price; }
    public String getSize(){ return size; }
    public void setSize(String size){ this.size=size; }
    public String getColor(){ return color; }
    public void setColor(String color){ this.color=color; }
}
