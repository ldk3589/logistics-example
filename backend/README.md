# backend
[English](README.md) | [中文](README.zh-CN.md)

Backend service for the RBAC permission management system.

This module is built with Spring Boot and provides:

- authentication and registration
- user management
- role management
- permission management
- menu management
- department management
- order management
- login log and operation log
- RBAC-based access control
- data scope control for orders

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- JWT
- MyBatis-Plus
- MySQL
- Maven

---

## Module Structure

    backend                           # Backend folder
    ├── .mvn                          # Maven tools
    ├── src                           # Source code
    │   ├── main                      # Main code
    │   │   ├── java/com/dk/logistics # Java code for your app
    │   │   │   ├── common            # Shared tools
    │   │   │   ├── config            # App settings
    │   │   │   ├── framework         # Base code
    │   │   │   └── module            # Business parts
    │   │   │       ├── auth          # Login and access
    │   │   │       ├── order         # Order part
    │   │   │       └── system        # System part
    │   │   │           ├── dept      # Department part
    │   │   │           ├── log       # Record part
    │   │   │           ├── menu      # Menu part
    │   │   │           ├── permission# Access rules
    │   │   │           ├── role      # User roles
    │   │   │           └── user      # User part
    │   │   └── resources             # Files and data
    │   └── test                      # Test code
    ├── .gitattributes                # Git rules
    ├── .gitignore                    # Hide from Git
    ├── mvnw                          # Maven tool (Mac/Linux)
    ├── mvnw.cmd                      # Maven tool (Windows)
    ├── pom.xml                       # Tool list
    ├── README.md                     # Read me (English)
    └── README.zh-CN.md               # Read me (Chinese)

---

## Main Features

### auth

- register
- login
- return current user info
- return current user menus
- return current user permissions

### system/user

- list users
- get user detail
- add user
- update user
- delete user
- change user status
- assign roles to user

### system/role

- list roles
- get role detail
- add role
- update role
- delete role
- assign permissions
- assign menus

### system/permission

- list permissions
- get permission detail
- add permission
- update permission
- delete permission

### system/menu

- list menu tree
- get menu detail
- add menu
- update menu
- delete menu

### system/dept

- list department tree
- get department detail
- add department
- update department
- delete department

### system/log

- query login logs
- query operation logs

### order

- list orders
- add order
- update order
- delete order
- assign order
- complete order

---

## RBAC Design

The system uses a standard RBAC model:

- User -> Role
- Role -> Permission
- Role -> Menu

Notes:

- users do not get permissions directly
- users get permissions through roles
- backend permission check is the real security boundary
- frontend display is only for better user experience

---

## Data Scope

The order module supports data scope control.

Common data scopes include:

- ALL
- DEPT
- DEPT_AND_CHILD
- SELF

This means different roles can see different order data.

---

## Database Tables

Typical tables include:

- sys_user
- sys_role
- sys_permission
- sys_menu
- sys_dept
- sys_user_role
- sys_role_permission
- sys_role_menu
- orders
- sys_login_log
- sys_operation_log

---

## Run Backend

### 1. Configure database

Please edit the database configuration in the backend resource file.

Typical items:

    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/your_database_name
        username: root
        password: your_password

### 2. Start service

    cd backend
    mvn spring-boot:run

Default address:

    http://localhost:8080

---

## API Style

The backend uses:

- RESTful API
- unified response structure
- global exception handling
- JWT authentication
- method-level permission control

---

## Notes

Before testing, please make sure:

- database tables are initialized
- test roles and permissions are inserted
- user passwords are encoded correctly
- menu paths match frontend router paths

If login fails, first check:

- database connection
- password encoding
- role / permission seed data
- JWT configuration

---

## Future Improvements

- Swagger polishing
- Docker deployment
- more unit tests
- export functions
- better data permission design