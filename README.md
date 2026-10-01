# Thiệu_shop

Website bán hàng online bằng Spring Boot + MySQL + Thymeleaf.

## Công nghệ
- Java 21
- Spring Boot 3.5.5
- Spring Data JPA / Hibernate
- MySQL Server
- MySQL Workbench
- Thymeleaf
- Maven

## Chức năng
- Đăng ký, đăng nhập
- Xem/tìm kiếm sản phẩm
- Chi tiết sản phẩm, size, màu
- Giỏ hàng
- Đặt hàng
- COD và chuyển khoản ngân hàng + QR TPBank
- Lịch sử và chi tiết đơn hàng
- Hủy đơn và hoàn tồn kho
- Admin quản lý sản phẩm và trạng thái đơn

## Chạy nhanh
```bat
cd /d C:\Thiệu_shop
C:\apache-maven-3.9.16\bin\mvn.cmd clean package
java -jar target\shop-online-1.0.0.jar
```

Mở `http://localhost:8080`.

Admin:
- Username: `admin`
- Password: `admin123`

## Database
MySQL Server chạy ở `localhost:3306`, database `shop_online`.
MySQL Workbench dùng để mở/chạy `database/shop_online.sql` và xem dữ liệu.

Cấu hình kết nối nằm tại:
`src/main/resources/application.properties`
