# logistics-example
[English](README.md) | [中文](README.zh-CN.md)

🚚 Logistics Example

A **full-stack logistics order management system** built with **Spring Boot + Vue 3**, featuring user authentication, role-based access control, order lifecycle management, and statistical analysis.

This project is designed as a **learning-oriented project**, suitable for demonstrating practical skills in **Java backend**, **full-stack development**, and **enterprise-style system design**.

---

## 📁 Project Structure

```
logistics-example
├── backend/                # Backend: Spring Boot project
│   ├── src/
│   ├── logs/               # Backend logs (auto-generated)
│   ├── pom.xml
│   └── README.md           # Backend README
│
├── frontend/               # Frontend: Vue 3 + Vite project
│   ├── src/
│   ├── logs/               # Frontend logs (optional)
│   ├── package.json
│   └── README.md           # Frontend README
│
├── .gitattributes
└── README.md               # Project root README (this file)
```

---

## 🚀 Features

### ✅ Implemented Features

#### 👤 User & Authorization
- User registration and login
- Automatic trimming of leading/trailing spaces in username & password
- Validation to prevent spaces inside username or password
- Role-based access control:
    - USER
    - ADMIN_L2
    - ADMIN_L1
- Admin registration protected by admin passwords

#### 📦 Order Management
- Create orders
- Update order status (PROCESSING / COMPLETED / CANCELLED)
- Assign orders to administrators
- Role-based order visibility

#### 📊 Statistics & Analytics
- Order count statistics
- Total order amount statistics
- Time-based aggregation:
    - Weekly
    - Monthly
    - Yearly
    - Historical (all-time)
- Line charts rendered with **ECharts**

#### 🔐 Security & Engineering
- JWT-based authentication
- Spring Security integration
- Axios request/response interceptors
- Global exception handling
- Automatic backend & frontend logging

---

## 🧱 Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security (JWT)
- MyBatis-Plus
- MySQL
- Maven
- Logback (file-based logging)

### Frontend
- Vue 3
- Vite
- Element Plus
- Axios
- Vue Router
- ECharts

---

## ⚙️ Prerequisites

- JDK ≥ 17 (Recommended: 21)
- Node.js ≥ 18
- MySQL ≥ 8.0
- Maven ≥ 3.8

---

## 🛠️ Getting Started

### 1️⃣ Backend Setup

#### Step 1: Create Database

```sql
CREATE DATABASE logistics DEFAULT CHARACTER SET utf8mb4;
```

#### Step 2: Configure Database

File path:

```
backend/src/main/resources/application-dev.yml
```

Example:

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/logistics?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

#### Step 3: Start Backend

```bash
cd backend
mvn spring-boot:run
```

Backend will be available at:

```
http://localhost:8080
```

---

### 2️⃣ Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend will be available at:

```
http://localhost:5173
```

---

## 🔗 API Example

### Login API

```
POST /auth/login
```

Request Body:

```json
{
  "username": "test",
  "password": "123456"
}
```

Response Example:

```json
{
  "token": "xxxx.yyyy.zzzz",
  "userInfo": {
    "id": 1,
    "username": "test",
    "role": "USER"
  }
}
```

---

## 🔐 Authentication Flow

- **Registration**
    - Input validation on username & password
    - Default role: USER
    - Admin roles require correct admin password

- **Login**
    - Successful login returns JWT token and user info
    - Token stored in `localStorage`
    - Frontend route guards protect secured pages

---

## 📄 Logging

### Backend Logs
- Automatically written to:

```
backend/logs/
```

- Includes:
    - Application startup logs
    - SQL execution logs
    - API access & exception logs

### Frontend Logs
- Can be extended to record:
    - Request errors
    - Runtime exceptions
    - User behavior events

---

## 🎯 Project Purpose

- ✔ Backend / Full-stack interview project
- ✔ Demonstrates real-world Spring Boot + Vue architecture
- ✔ Clean structure, easy to extend
- ✔ Suitable for enterprise-style system design discussions

---

## 📌 Future Improvements

- Pagination & advanced search
- Admin dashboard
- Excel export
- Audit logs
- Docker-based deployment

---
