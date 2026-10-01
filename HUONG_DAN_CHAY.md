# Thiệu_shop – bản hoàn thiện

## Chức năng hiện có
- Đăng ký / đăng nhập / đăng xuất.
- Trang sản phẩm, tìm kiếm theo tên và danh mục.
- Chi tiết sản phẩm.
- Quản trị sản phẩm: thêm và xóa sản phẩm, nhập size và màu.
- Giỏ hàng: tăng/giảm số lượng, xóa sản phẩm, kiểm tra tồn kho.
- Lưu riêng sản phẩm + size + màu + số lượng trong giỏ.
- Đặt hàng và lưu `orders` + `order_items` trong transaction.
- Lịch sử đơn hàng, xem chi tiết đơn hàng.
- Khách hàng có thể hủy đơn ở trạng thái `PENDING` hoặc `CONFIRMED`; tồn kho được hoàn lại.
- Admin xem đơn hàng, khách hàng, địa chỉ, sản phẩm và cập nhật trạng thái.
- Trạng thái đơn: `PENDING` → `CONFIRMED` → `SHIPPING` → `COMPLETED`; có thể hủy từ `PENDING`/`CONFIRMED`.
- Thanh toán COD.
- Thanh toán chuyển khoản ngân hàng, có QR TPBank hiển thị khi chọn chuyển khoản.

## Cấu hình hiện tại: MySQL Server + MySQL Workbench
Ứng dụng **không cần XAMPP**.

- Database: `shop_online`
- Host: `localhost`
- Port: `3306`
- User: `root`
- Password: `140206` (theo `application.properties` hiện tại)

MySQL Workbench chỉ là công cụ kết nối/quản trị; MySQL Server (`mysqld.exe`) mới là dịch vụ cơ sở dữ liệu.

## Cách chuẩn bị database
Mở MySQL Workbench và chạy file:
`database/shop_online.sql`

File này tạo/chọn database `shop_online`. Các bảng sẽ được Spring Boot + JPA tạo/cập nhật tự động khi chạy vì:
`spring.jpa.hibernate.ddl-auto=update`

## Cách chạy trên Windows
Mở CMD:

```bat
cd /d C:\Thiệu_shop
C:\apache-maven-3.9.16\bin\mvn.cmd clean package
java -jar target\shop-online-1.0.0.jar
```

Nếu thấy `Started ShopOnlineApplication` thì mở:
`http://localhost:8080`

Nếu project nằm ở ổ khác, thay đường dẫn `C:\Thiệu_shop` bằng đường dẫn thực tế.

## Tài khoản admin mặc định
- Username: `admin`
- Password: `admin123`

## Thanh toán chuyển khoản
Khi khách chọn `Chuyển khoản ngân hàng`, trang giỏ hàng hiển thị:
- Ngân hàng: TPBank
- Số tài khoản: 57114022006
- Chủ tài khoản: TRẦN DUY THIỆU
- Mã QR: `src/main/resources/static/image/QR Thanh Toan.png`

## Lưu ý
- Không cần bật MySQL trong XAMPP.
- Không xóa database `shop_online` nếu muốn giữ dữ liệu hiện tại.
- Nếu mật khẩu MySQL Server thay đổi, sửa `spring.datasource.password` trong `src/main/resources/application.properties`.
- Sau khi sửa HTML/CSS/Java, nên chạy lại `mvn clean package` trước khi chạy JAR.
