# frontend
[English](README.md) | [中文](README-zh.md)

Frontend module for the RBAC permission management system.

This module is built with Vue 3 and Element Plus.

It provides pages for:

- login and registration
- dashboard
- user management
- role management
- permission management
- menu management
- department management
- order management
- login log
- operation log

---

## Tech Stack

- Vue 3
- Vite
- Vue Router
- Element Plus
- Axios

---

## Project Structure
    
    frontend                          # Frontend folder
    ├── public                        # Public files
    ├── src                           # Source code
    │   ├── layout                    # Page layout
    │   ├── router                    # Page links
    │   ├── utils                     # Helpful tools
    │   ├── views                     # Page views
    │   ├── App.vue                   # Main Vue page
    │   └── main.js                   # Main JS file
    ├── .gitattributes                # Git rules
    ├── .gitignore                    # Hide from Git
    ├── index.html                    # Main web page
    ├── package.json                  # Tool list
    ├── package-lock.json             # Locked tools
    ├── vite.config.js                # Vite settings
    ├── README.md                     # Read me (English)
    └── README.zh-CN.md               # Read me (Chinese)

---

## Main Pages

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

## Frontend Functions

### Authentication

- login page
- register page
- token storage
- route guard
- logout

### Request Handling

- Axios request interceptor
- attach JWT token automatically
- unified error message
- redirect to login when token expires

### Permission Display

- dynamic menu display
- button display based on permission code
- page access controlled by login state

### Business Pages

- user CRUD related pages
- role CRUD related pages
- permission CRUD related pages
- menu CRUD related pages
- department CRUD related pages
- order CRUD related pages
- login log and operation log pages

---

## Run Frontend

    cd frontend
    npm install
    npm run dev

Default address:

    http://localhost:5173

---

## Proxy Configuration

The frontend usually proxies requests to backend service.

Typical local development target:

    http://localhost:8080

Please check the proxy settings in:

    vite.config.js

---

## Router Notes

The frontend router path must match the menu path returned by backend.

For example, if backend menu path is:

    /system/user
    /system/role
    /system/menu

then frontend router should also support these paths.

If a page becomes blank, first check:

- whether the frontend route exists
- whether backend menu path matches router path
- whether current user has permission
- whether the API path is correct

---

## Permission Notes

Frontend button display is based on permission code.

For example:

- sys:user:add
- sys:user:update
- sys:user:delete
- sys:role:add
- sys:permission:update
- order:assign

But real security still depends on backend permission checks.

---

## Suggested Improvements

- dynamic route registration
- reusable permission directive
- reusable table and form components
- better department selector
- better user selector for order assignment
- export and import features