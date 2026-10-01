package com.shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "san_pham")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_san_pham", nullable=false, length=150)
    private String name;

    @Column(name = "gia", nullable=false)
    private double price;

    @Column(name = "so_luong", nullable=false)
    private int quantity;

    @Column(name = "mo_ta", length=1000)
    private String description;

    @Column(name = "hinh_anh", length=500)
    private String image;

    @Column(name = "danh_muc", length=100)
    private String category;

    @Column(name = "kich_thuoc", length=500)
    private String availableSizes;

    @Column(name = "mau_sac", length=500)
    private String availableColors;

    public Product() {}
    public Long getId(){ return id; }
    public String getName(){ return name; }
    public void setName(String name){ this.name=name; }
    public double getPrice(){ return price; }
    public void setPrice(double price){ this.price=price; }
    public int getQuantity(){ return quantity; }
    public void setQuantity(int quantity){ this.quantity=quantity; }
    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description=description; }
    public String getImage(){ return image; }
    public void setImage(String image){ this.image=image; }
    public String getCategory(){ return category; }
    public void setCategory(String category){ this.category=category; }
    public String getAvailableSizes(){ return availableSizes; }
    public void setAvailableSizes(String availableSizes){ this.availableSizes=availableSizes; }
    public String getAvailableColors(){ return availableColors; }
    public void setAvailableColors(String availableColors){ this.availableColors=availableColors; }
}
