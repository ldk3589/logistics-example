# logistics-frontend

🚚 Logistics Management System · Frontend

This project is the **frontend part** of `logistics-example`, built with **Vue 3** and **Element Plus**.  
It works with a Spring Boot backend and implements **JWT-based authentication**,  
**role-based dynamic menus**, **order management**, and **order statistics visualization**.

---

## 🧱 Tech Stack

- **Framework**: Vue 3 (Composition API)
- **Build Tool**: Vite
- **UI Library**: Element Plus
- **HTTP Client**: Axios
- **Chart Library**: ECharts
- **Routing**: Vue Router
- **State / Auth**: LocalStorage + JWT
- **Logging**: Custom Frontend Logger (file + console)

---

## 📁 Project Structure

```
frontend
├── src
│   ├── api/                 # API request modules
│   ├── assets/              # Static assets
│   ├── components/          # Reusable components
│   ├── layout/              # Layout & dynamic menu
│   ├── router/              # Vue Router config
│   ├── utils/
│   │   ├── auth.js           # Token & role helpers
│   │   ├── logger.js         # Frontend logging utility
│   │   └── request.js        # Axios instance & interceptors
│   ├── views/
│   │   ├── Login.vue
│   │   ├── Order.vue
│   │   ├── OrderStat.vue
│   │   └── ...
│   ├── App.vue
│   └── main.js
│
├── public/
├── logs/                    # Frontend logs (runtime)
├── vite.config.js
├── package.json
└── README.md
```

---

## 🔐 Authentication & Authorization

### Login

- User logs in with username & password
- Backend returns:
    - JWT Token
    - User info (id, username, role)
- Token is stored in `localStorage`

### Authorization

- Dynamic menus rendered based on `role`
- Route guards protect pages
- Axios interceptor injects JWT automatically

---

## 👤 Role-Based UI Behavior

| Role | Permissions |
|-----|-------------|
| USER | View & create own orders, view own statistics |
| ADMIN_L2 | Manage assigned orders, view own statistics |
| ADMIN_L1 | Full access: all orders, assignment, statistics |

Menus and pages are automatically filtered by role.

---

## 📝 Registration Rules

- Username & password:
    - Leading and trailing spaces are trimmed
    - Spaces in the middle are **not allowed**
- No role selection in UI
- Admin registration requires correct **admin password**
    - Wrong password → registered as normal user

---

## 📦 Order Module

- Order list
- Order creation
- Status update
- Assignment (ADMIN_L1 only)
- Permission-sensitive operations

---

## 📊 Order Statistics Module

- Statistics by:
    - Week
    - Month
    - Year
    - All-time
- Data displayed via:
    - Line charts (ECharts)
    - Table view
- Same API, different results based on role

---

## 📄 Frontend Logging

### Logger Utility

- File: `src/utils/logger.js`
- Automatically logs:
    - API errors
    - Runtime errors
    - Custom debug info

### Log Output Directory

```
frontend/logs/
```

Logs are persisted for debugging and interview demonstration.

---

## ▶️ Project Setup

### Install dependencies

```bash
cd frontend
npm install
```

### Run development server

```bash
npm run dev
```

Default access:

```
http://localhost:5173
```

---

## 🔗 Backend Dependency

This frontend depends on the backend service:

```
http://localhost:8080
```

Make sure backend is running before login.

---

## 🎯 Design Goals

- Clear separation of concerns
- Role-driven UI & logic
- Minimal but complete feature set
- Close to real enterprise frontend projects

---

## 📌 Notes

- This project is intended for **learning showcase**
- Emphasizes correctness, clarity, and maintainability

---
