# logistics-example

[English](README.md) | [中文](README.zh-CN.md)

A full-stack RBAC permission management system built with Spring Boot and Vue 3.

This project focuses on common enterprise backend scenarios, including:

- authentication and registration
- user, role, permission, menu, and department management
- order business module
- login log and operation log
- role-based access control
- frontend dynamic menu rendering

It is suitable as:

- a Java backend practice project
- a full-stack integration practice project
- an interview / portfolio project

---

## Features

### Authentication

- user registration
- user login
- JWT token authentication
- frontend route guard
- Axios request interceptor

### RBAC Management

- user management
- role management
- permission management
- menu management
- department management
- assign roles to users
- assign permissions to roles
- assign menus to roles

### Business Module

- order list
- create order
- assign order
- complete order
- delete order
- data visibility based on role and data scope

### Logs

- login log query
- operation log query

---

## Tech Stack

### Backend

- Java 21
- Spring Boot 3
- Spring Security
- JWT
- MyBatis-Plus
- MySQL
- Maven

### Frontend

- Vue 3
- Vite
- Vue Router
- Element Plus
- Axios

---

## Project Structure

    logistics-example             # Main folder
    ├── backend                   # Backend part (Java)
    │   ├── .mvn                  # Maven tool files
    │   ├── src                   # Source code folder
    │   │   ├── main              # Main code
    │   │   │   ├── java          # Java code
    │   │   │   │   └── com
    │   │   │   │       └── dk
    │   │   │   │           └── logistics # Your app code
    │   │   │   └── resources     # App settings and data
    │   │   └── test              # Code to test the app
    │   ├── .gitattributes        # Git settings
    │   ├── .gitignore            # Files to hide from Git
    │   ├── mvnw                  # Maven tool (for Mac/Linux)
    │   ├── mvnw.cmd              # Maven tool (for Windows)
    │   ├── pom.xml               # Backend list of tools
    │   └── README.zh-CN.md       # Read me file (Chinese)
    │
    ├── frontend                  # Frontend part (Vue)
    │   ├── public                # Public files
    │   ├── src                   # Source code folder
    │   │   ├── layout            # Page layout
    │   │   ├── router            # Page links
    │   │   ├── utils             # Helpful tools
    │   │   ├── views             # Page views
    │   │   ├── App.vue           # Main Vue page
    │   │   └── main.js           # Main JS file
    │   ├── .gitattributes        # Git settings
    │   ├── .gitignore            # Files to hide from Git
    │   ├── index.html            # Main web page
    │   ├── package.json          # Frontend list of tools
    │   ├── package-lock.json     # Locked tool versions
    │   ├── vite.config.js        # Vite tool settings
    │   ├── README.md             # Read me file (English)
    │   └── README.zh-CN.md       # Read me file (Chinese)
    │
    ├── .gitignore                # Files to hide from Git
    ├── README.md                 # Read me file (English)
    └── README-zh.md              # Read me file (Chinese)

---

## Backend Modules

- auth
    - login
    - register
    - current user menus

- system/user
    - user CRUD
    - assign roles

- system/role
    - role CRUD
    - assign permissions
    - assign menus

- system/permission
    - permission CRUD

- system/menu
    - menu CRUD
    - menu tree

- system/dept
    - department tree
    - department CRUD

- system/log
    - login log
    - operation log

- order
    - order CRUD
    - assign / complete order
    - data scope filtering

---

## Frontend Pages

- Login
- Dashboard
- User Management
- Role Management
- Permission Management
- Menu Management
- Department Management
- Order Management
- Login Log
- Operation Log

---

## Quick Start

### 1. Clone Project

    git clone https://github.com/ldk3589/logistics-example.git
    cd logistics-example

### 2. Start Backend

    cd backend
    mvn spring-boot:run

Backend default address:

    http://localhost:8080

### 3. Start Frontend

    cd frontend
    npm install
    npm run dev

Frontend default address:

    http://localhost:5173

---

## Database

Please prepare a MySQL database first, then configure the database connection in the backend configuration file.

Typical configuration items:

- database host
- port
- database name
- username
- password

You also need to initialize:

- user table
- role table
- permission table
- menu table
- department table
- user-role relation table
- role-permission relation table
- role-menu relation table
- order table
- login log table
- operation log table

---

## Permission Design

The system follows a typical RBAC model:

- User -> Role
- Role -> Permission
- Role -> Menu

In this project:

- users do not get permissions directly
- users get permissions through roles
- frontend button display depends on permission codes
- actual security control still depends on backend permission checks

---

## Default Notes

Before testing the project, please make sure:

- menu paths in database match frontend router paths
- role and permission seed data are correct
- user passwords are encoded correctly
- backend API paths and frontend request paths are consistent

If some pages are blank, first check:

- frontend router path
- menu path from database
- whether the current user has permission
- whether the backend API path is correct

---

## Future Improvements

- dynamic route registration
- export data
- Swagger documentation polishing
- Docker deployment
- unit tests
- page-level and button-level permission wrappers
- better department selector and user selector components

---

## License

This project is for learning and personal showcase use.