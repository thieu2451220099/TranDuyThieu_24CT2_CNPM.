-- =====================================================
-- DATABASE: THIỆU_SHOP
-- =====================================================

DROP DATABASE IF EXISTS shop_online;

CREATE DATABASE shop_online
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE shop_online;

-- =====================================================
-- 1. NGƯỜI DÙNG
-- =====================================================
CREATE TABLE nguoi_dung (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_dang_nhap VARCHAR(100) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    vai_tro VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    ho_ten VARCHAR(100),
    so_dien_thoai VARCHAR(20),
    dia_chi VARCHAR(255)
);

-- =====================================================
-- 2. SẢN PHẨM
-- =====================================================
CREATE TABLE san_pham (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_san_pham VARCHAR(150) NOT NULL,
    gia DOUBLE NOT NULL,
    so_luong INT NOT NULL DEFAULT 0,
    mo_ta VARCHAR(1000),
    hinh_anh VARCHAR(500),
    danh_muc VARCHAR(100),
    kich_thuoc VARCHAR(500),
    mau_sac VARCHAR(500)
);

-- =====================================================
-- 3. ĐƠN HÀNG
-- =====================================================
CREATE TABLE don_hang (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id BIGINT NOT NULL,
    tong_tien DOUBLE NOT NULL DEFAULT 0,
    phuong_thuc_thanh_toan VARCHAR(30) NOT NULL DEFAULT 'COD',
    trang_thai_thanh_toan VARCHAR(30) NOT NULL DEFAULT 'UNPAID',
    trang_thai VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    ngay_tao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dia_chi_giao_hang VARCHAR(255),
    CONSTRAINT fk_don_hang_nguoi_dung
        FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- =====================================================
-- 4. CHI TIẾT ĐƠN HÀNG
-- =====================================================
CREATE TABLE chi_tiet_don_hang (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    don_hang_id BIGINT NOT NULL,
    san_pham_id BIGINT NOT NULL,
    so_luong INT NOT NULL,
    gia DOUBLE NOT NULL,
    kich_thuoc VARCHAR(100),
    mau_sac VARCHAR(100),
    CONSTRAINT fk_chi_tiet_don_hang
        FOREIGN KEY (don_hang_id) REFERENCES don_hang(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_chi_tiet_san_pham
        FOREIGN KEY (san_pham_id) REFERENCES san_pham(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- =====================================================
-- KIỂM TRA
-- =====================================================
USE shop_online;
SHOW TABLES;
SELECT * FROM nguoi_dung;
SELECT * FROM san_pham;
SELECT * FROM don_hang;
SELECT * FROM chi_tiet_don_hang;