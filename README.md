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

## Yêu cầu môi trường
- Java 17+
- Maven Wrapper (đi kèm trong project)

## Hướng dẫn chạy dự án
```bash
cd employee-management
./mvnw clean test
./mvnw spring-boot:run
```

## Các đường dẫn Web UI (Thymeleaf)
- `http://localhost:8080/employees/list`: Xem danh sách nhân viên từ DB và tìm kiếm
- `http://localhost:8080/employees/add`: Form thêm mới nhân viên có validation
- `http://localhost:8080/employees/search`: Tìm kiếm nhân viên theo tên / phòng ban

## Danh sách REST API
- `GET /hello`: Kiểm tra ứng dụng hoạt động
- `GET /api/employees`: Lấy danh sách nhân viên từ DB (hỗ trợ query param `name`, `department`)
- `GET /api/employees/{id}`: Lấy chi tiết nhân viên theo id
- `POST /api/employees`: Tạo nhân viên mới (validation, sinh mã tự động, liên kết phòng ban)
- `PUT /api/employees/{id}`: Cập nhật thông tin nhân viên
- `DELETE /api/employees/{id}`: Xóa nhân viên theo id
- `GET /api/employees/statistics`: Thống kê nhân viên theo phòng ban
