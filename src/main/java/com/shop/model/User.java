package com.shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nguoi_dung")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "mat_khau", nullable = false, length = 255)
    private String password;

    @Column(name = "vai_tro", nullable = false, length = 20)
    private String role = "CUSTOMER";

    @Column(name = "ho_ten", length = 100)
    private String fullName;

    @Column(name = "so_dien_thoai", length = 20)
    private String phone;

    @Column(name = "dia_chi", length = 255)
    private String address;

    public User() {}
    public Long getId(){ return id; }
    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username=username; }
    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password=password; }
    public String getRole(){ return role; }
    public void setRole(String role){ this.role=role; }
    public String getFullName(){ return fullName; }
    public void setFullName(String fullName){ this.fullName=fullName; }
    public String getPhone(){ return phone; }
    public void setPhone(String phone){ this.phone=phone; }
    public String getAddress(){ return address; }
    public void setAddress(String address){ this.address=address; }
}
