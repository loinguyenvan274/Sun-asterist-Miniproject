# Employee Management System - Mini Project

## Giới thiệu
Dự án thực hành Employee Management System xây dựng bằng Spring Boot.

## Các tính năng chính
- Module 1 (Lab 1): Khởi tạo Spring Boot application và REST API `/hello`.
- Module 2 (Lab 2): Cấu hình Custom Bean (`ModelMapper`) trong `@Configuration`, tạo `UtilityService` (`@Service`) chuẩn hóa dữ liệu và sinh mã nhân viên tự động, tiêm phụ thuộc qua Constructor Injection.
- Module 3 (Lab 3): Xây dựng REST API hoàn chỉnh quản lý nhân viên với dữ liệu in-memory (`GET`, `POST`, `PUT`, `DELETE`, thống kê phòng ban).

## Yêu cầu môi trường
- Java 17+
- Maven Wrapper (đi kèm trong project)

## Hướng dẫn chạy dự án
```bash
cd employee-management
./mvnw clean test
./mvnw spring-boot:run
```

## Danh sách API
- `GET /hello`: Kiểm tra ứng dụng hoạt động
- `GET /api/employees`: Lấy danh sách nhân viên (hỗ trợ query param `name`, `department`)
- `GET /api/employees/{id}`: Lấy chi tiết nhân viên theo id
- `POST /api/employees`: Tạo nhân viên mới
- `PUT /api/employees/{id}`: Cập nhật thông tin nhân viên
- `DELETE /api/employees/{id}`: Xóa nhân viên theo id
- `GET /api/employees/statistics`: Thống kê nhân viên theo phòng ban
