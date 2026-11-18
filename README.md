# Employee Management System - Mini Project

## Giới thiệu
Dự án thực hành Employee Management System xây dựng bằng Spring Boot.

## Các tính năng chính theo Modules
- Module 1 (Lab 1): Khởi tạo Spring Boot application và REST API `/hello`.
- Module 2 (Lab 2): Cấu hình Custom Bean (`ModelMapper`) trong `@Configuration`, tạo `UtilityService` (`@Service`) chuẩn hóa dữ liệu và sinh mã nhân viên tự động, tiêm phụ thuộc qua Constructor Injection.
- Module 3 (Lab 3): Xây dựng REST API hoàn chỉnh quản lý nhân viên với dữ liệu in-memory (`GET`, `POST`, `PUT`, `DELETE`, thống kê phòng ban).
- Module 4 (Lab 4): Tích hợp Spring Data JPA với Database, các Entity `Employee` và `Department` (quan hệ Many-to-One), `EmployeeRepository` & `DepartmentRepository`, CRUD với DB và tìm kiếm nhân viên theo tên hoặc phòng ban.
- Module 5 (Lab 5): Validation & Exception Handling toàn diện (`@NotNull`, `@Size`, `@Email`, `@Valid`, `BindingResult`, `@RestControllerAdvice`, xử lý 404 Not Found và thông báo lỗi rõ ràng khi sai định dạng dữ liệu).
- Module 6 (Lab 6): Spring Boot Web MVC + Thymeleaf (Giao diện danh sách `/employees/list`, form thêm mới `/employees/add`, tìm kiếm nhân viên theo tên/phòng ban).
- Module 7 (Lab 7): Logging với SLF4J + Logback khi thêm/sửa/xóa nhân viên, tách cấu hình Database theo profile dev (H2 in-memory) và prod (MySQL).
- Module 8 (Lab 8): Advanced Spring Boot: Spring Boot Actuator (`/actuator/health`, `/actuator/metrics`), Scheduled task log "System running" mỗi 30s, Caching với Caffeine cho API báo cáo tổng số nhân viên (TTL 1 phút).
- Module 9 (Lab 9): Spring Security Basics: Phân quyền RBAC (Role-Based Access Control) với `User` entity (`ROLE_USER`, `ROLE_ADMIN`), Đăng ký & Đăng nhập (Web form + REST JWT Auth), USER chỉ được xem danh sách, ADMIN có toàn quyền CRUD.
- Module 10 (Lab 10): Reporting & Analytics: Thống kê số lượng nhân viên theo phòng ban, tổng số nhân viên, phân tích lương (trung bình, nhỏ nhất, lớn nhất) bằng `@Query` trong Repository, hiển thị qua REST API và trang web `/employees/statistics`.

## Yêu cầu môi trường
- Java 17+
- Maven Wrapper (đi kèm trong project)

## Hướng dẫn chạy dự án
```bash
cd employee-management
./mvnw clean test
./mvnw spring-boot:run
```

## Tài khoản mẫu mặc định
- **Admin**: `admin` / `admin123` (Role `ROLE_ADMIN` - toàn quyền CRUD)
- **User**: `user` / `user123` (Role `ROLE_USER` - chỉ xem danh sách và thống kê)

## Các đường dẫn Web UI (Thymeleaf)
- `http://localhost:8080/employees/list`: Xem danh sách nhân viên từ DB và tìm kiếm
- `http://localhost:8080/employees/add`: Form thêm mới nhân viên (yêu cầu ADMIN)
- `http://localhost:8080/employees/search`: Tìm kiếm nhân viên theo tên / phòng ban
- `http://localhost:8080/employees/statistics`: Xem báo cáo thống kê trực quan
- `http://localhost:8080/login`: Trang đăng nhập
- `http://localhost:8080/register`: Trang đăng ký tài khoản

## Danh sách REST API
- `POST /api/auth/register`: Đăng ký tài khoản mới
- `POST /api/auth/login`: Đăng nhập và nhận JWT Bearer Token
- `GET /hello`: Kiểm tra ứng dụng hoạt động
- `GET /actuator/health`: Kiểm tra sức khỏe hệ thống (Actuator)
- `GET /api/employees`: Lấy danh sách nhân viên từ DB (USER hoặc ADMIN)
- `GET /api/employees/{id}`: Lấy chi tiết nhân viên theo id (USER hoặc ADMIN)
- `POST /api/employees`: Tạo nhân viên mới (chỉ ADMIN)
- `PUT /api/employees/{id}`: Cập nhật thông tin nhân viên (chỉ ADMIN)
- `DELETE /api/employees/{id}`: Xóa nhân viên theo id (chỉ ADMIN)
- `GET /api/employees/count`: Tổng số nhân viên (có Caching 1 phút)
- `GET /api/employees/statistics`: Thống kê nhân viên theo phòng ban
- `GET /api/employees/reports/summary`: Báo cáo tổng hợp toàn hệ thống
- `GET /api/employees/analytics/salary`: Phân tích lương theo phòng ban
