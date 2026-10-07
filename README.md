# Session 02: Microservices Course Management Architecture

Tài liệu phân tích và thiết kế kiến trúc hệ thống Microservices cho nền tảng **Quản lý Khóa học Trực tuyến (E-Learning / LMS)**.

---

## 📌 Tài liệu Thiết Kế Chi Tiết

Xem toàn bộ đặc tả API, sơ đồ kiến trúc, mô hình CSDL và cấu hình tại:

👉 **[MICROSERVICES_COURSE_MANAGEMENT_DESIGN.md](file:///d:/devops/Session02-Microservices/MICROSERVICES_COURSE_MANAGEMENT_DESIGN.md)**

---

## 🚀 4 Microservices Trọng Tâm

| Service | Port | Database | Nhiệm vụ chính |
| :--- | :---: | :--- | :--- |
| **Auth Service** | `8081` | `auth_db` | Đăng ký, đăng nhập, cấp JWT, quản lý phân quyền (Admin, Instructor, Student). |
| **Course Service** | `8082` | `course_db` | Quản lý danh mục, khóa học, chương học (Sections), bài học (Lessons). |
| **Enrollment Service** | `8083` | `enrollment_db` | Quản lý quyền học, ghi danh, theo dõi % tiến độ và đánh dấu hoàn thành bài học. |
| **Payment Service** | `8084` | `payment_db` | Tạo đơn hàng, kết nối cổng thanh toán (VNPay/MoMo), gọi kích hoạt ghi danh. |

---

## 🛠️ Yêu Cầu Kỹ Thuật (Tech Stack)

- **Java / JDK**: JDK 17 LTS
- **Framework**: Spring Boot (Spring Boot 3.x / 4.x)
- **Build Tool**: Gradle với Gradle Wrapper (`gradlew`, `gradlew.bat`)
- **Configuration**: Tập tin `application.properties` (Database connection, server port, JWT)
- **Cơ sở dữ liệu**: PostgreSQL 15+ (Database-per-Service: `auth_db`, `course_db`, `enrollment_db`, `payment_db`)

---

## 📂 Chuẩn Cấu Trúc Package Của Từng Service

```plaintext
com.elearning.<servicename>/
├── config/              # Các cấu hình Security, Web, Beans
├── constant/            # Enum, Hằng số, Mã lỗi (ErrorCode)
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
│   ├── request/         # Request DTOs kèm validation (@Valid)
│   └── response/        # Response DTOs, ApiResponse<T>
├── entity/              # JPA Entities (PostgreSQL tables)
├── exception/           # Custom Exceptions & GlobalExceptionHandler
├── repository/          # Spring Data JPA Repositories
└── service/             # Service interfaces & Service implementations (impl/)
```
