

# ☕ <b>The Eternal Appetite</b>  
### <i>A Spring Boot Café Website Project</i>  
Built with  Spring Boot | 💾 MySQL | 🧩 Thymeleaf | 💳 Razorpay  



---

 🏆 Overview
The Eternal Appetite is a full-featured Spring Boot café management system that allows users to browse menu items, place online orders, and make secure payments via Razorpay or Cash on Delivery.  
The application includes an admin panel for managing menu items, orders, and user queries, with a clean and responsive UI.

---

## 🚀 Features

### 👥 User Features
- User Registration & Login (managed using HttpSession)
- Browse café menu with categories and prices
- Add items to cart and confirm orders
- Payment via Razorpay Payment Gateway or Cash on Delivery
- View order history and details
- Contact form for customer queries
- Responsive design using Bootstrap

### 🧑‍💼 Admin Features
- Add / Edit / Delete menu items
- View and manage all orders
- Update order statuses (Pending, Preparing, Delivered)
- Manage customer messages and queries

---

## 🧠 Tech Stack

| Layer | Technologies Used |
|-------|-------------------|
| **Backend** | Spring Boot, Java, Spring MVC, JPA |
| **Frontend** | HTML, CSS, Bootstrap, Thymeleaf |
| **Database** | MySQL |
| **ORM** | Spring Data JPA |
| **Payment Integration** | Razorpay API |
| **Session Management** | HttpSession |
| **Build Tool** | Maven |

---

## 🏗️ Project Structure
com.example.The.Eternal.Appetite
├── Controller
│ ├── AdminController.java
│ ├── MenuController.java
│ ├── UserController.java
│ └── RazorpayOrderController.java
│
├── Entity
│ ├── User.java
│ ├── MenuItem.java
│ ├── Orders.java
│ ├── OrderItems.java
│ └── ContactQuery.java
│
├── Repository
│ ├── UserRepository.java
│ ├── MenuRepository.java
│ ├── OrdersRepository.java
│ └── ContactRepository.java
│
├── Service
│ ├── UserService.java
│ ├── MenuService.java
│ └── OrderService.java
│
└── DTO
└── CheckoutDTO.java


---

## ⚙️ How to Run Locally

### 1️⃣ Clone Repository
```bash
git clone https://github.com/AditiDalal2003/The-Eternal-Appetite_SpringBoot.git
cd The-Eternal-Appetite_SpringBoot

2️⃣ Configure Database

Create database in MySQL:

CREATE DATABASE cafe;


Add your MySQL credentials in application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/cafe
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update

3️⃣ Run Application
mvn spring-boot:run

4️⃣ Access in Browser
http://localhost:8080
```
## 💳  Razorpay Integration

The project integrates Razorpay Payment Gateway for secure online transactions.
API keys (key_id & key_secret) are excluded from this repository and must be set via environment variables locally.

## 🔒  Security Measures

.gitignore includes application.properties to avoid exposing credentials

Razorpay & Email credentials stored as environment variables

MySQL credentials hidden for security

 ## 🏁 Future Enhancements 

Add order tracking system (status updates)

Add PDF invoice download option after payment

## 👩‍💻 About the Developer

**Aditi Atul Dalal**  
🎓 B.E. in Computer Engineering | University of Mumbai    
🚀 Passionate about building scalable web applications using Java, Spring Boot, MySQL & React  


## ⭐ *If you found this project helpful, please give it a star on GitHub!*
