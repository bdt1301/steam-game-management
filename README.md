# 🎮 Steam Game Management Website

## ▶️ Project Requirements

This project was developed and tested using the following **personal development environment**:

### 🔧 Development Environment
- Java Development Kit (JDK) **17.0.12**
- Apache Maven **3.9.9**
- Build & Run via Maven/Command Line (jar)

### 🗄️ Database
- PostgreSQL Server
- Database connection configured in `application.properties`

---

## 🛠️ Technologies Used
| Layer | Technologies |
|------|-------------|
| Backend | Spring Boot (MVC, JPA/Hibernate, Security) |
| Frontend | HTML, CSS, JavaScript |
| UI Framework | Bootstrap |
| Template Engine | Thymeleaf |
| Database | PostgreSQL |

---

## 🧱 Backend Design

### 📦 Main Entities
- `Game`, `Publisher`, `Category`, `User`, `Notification`, `Record`

### 🔗 Entity Relationships
| Relationship | Description |
|-------------|------------|
| Publisher – Game | One-to-Many |
| Category – Game | Many-to-Many |
| User – Game | Many-to-Many (favorite games) |
| User – Notification | One-to-Many |
| Record | Zero-to-Zero (used to log Admin activities) |

---

## 🔐 Authentication & Authorization

### 🔑 Authentication
- Login and registration handled via **Spring Security filter chain**
- Passwords stored securely using **BCryptPasswordEncoder**
- Validates **duplicate username and email** during registration
- Prevents login with invalid or disabled accounts

### 🛡️ Authorization (RBAC)
Role-based access control is implemented using **Spring Security roles**.

| Role | Permissions |
|------|-------------|
| **Anonymous** | View games, publishers, categories |
| **User** | All Anonymous permissions<br>Manage favorite games<br>View and update personal profile<br>Receive notifications |
| **Admin** | All Anonymous permissions<br>Create, update, delete games, publishers, categories<br>Delete users<br>View admin activity logs |

---

## 🎨 Frontend Features
- 🔍 Search games by name
- 🎯 Filter games by name, categories, peak players, price range, date range
- ↕️ Table sorting
- 📄 Pagination
- 🖼️ Game details page with multiple images
- 🔐 Role-based UI rendering (Anonymous / User / Admin)
