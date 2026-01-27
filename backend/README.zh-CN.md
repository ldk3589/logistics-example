# logistics-backend

🚀 物流管理系统 · 后端服务

本项目是 **logistics-example** 的后端部分，基于 **Spring Boot** 构建，  
提供 **用户认证、权限控制、订单管理、订单统计分析** 等核心功能，  
采用 **JWT 无状态认证 + 基于角色的访问控制（RBAC）**。

---

## 🧱 技术栈

- **Java**：JDK 21
- **后端框架**：Spring Boot
- **安全框架**：Spring Security + JWT
- **ORM**：MyBatis-Plus
- **数据库**：MySQL 8.x
- **构建工具**：Maven
- **日志系统**：Logback（文件日志）

---

## 📁 项目结构说明

```
backend
├── src/main/java/com/dk/logistics
│   ├── config/              # 配置类（Security、跨域等）
│   ├── controller/          # 控制层（REST API）
│   ├── dto/                 # 数据传输对象
│   ├── entity/              # 实体类（User、Order 等）
│   ├── mapper/              # MyBatis-Plus Mapper 接口
│   ├── security/            # JWT、认证授权相关逻辑
│   ├── service/             # 业务接口层
│   ├── service/impl         # 业务实现层
│   └── LogisticsApplication.java
│
├── src/main/resources
│   ├── mapper/              # Mapper XML（如有）
│   ├── application.yml
│   ├── application-dev.yml
│   └── logback-spring.xml   # 日志配置文件
│
├── logs/                    # 后端运行日志（自动生成）
├── pom.xml
└── README.md
```

---

## 🔐 认证与权限设计

### 用户角色说明

| 角色 | 含义 |
|------|------|
| USER | 普通用户 |
| ADMIN_L2 | 二级管理员 |
| ADMIN_L1 | 一级管理员 |

---

## 📝 注册规则说明

- **用户名 / 密码**
    - 自动去除首尾空格
    - 中间不允许包含空格
- **默认角色**
    - 普通用户（USER）
- **管理员注册**
    - 需要输入正确的管理员密码
    - 管理员密码分为一级 / 二级
    - 输入错误 → 自动降级为普通用户

---

## 🔑 登录说明

- 登录成功后返回：
    - JWT Token
    - 用户基础信息（id、用户名、角色）
- 所有受保护接口必须携带 Token

---

## 🛡️ 安全设计

- JWT 无状态认证
- Spring Security 过滤器链
- 基于角色的接口访问控制（`@PreAuthorize`）
- 自定义 `UserDetailsService`
- 使用 `BCryptPasswordEncoder` 进行密码加密

---

## 📦 订单管理模块

### 功能概述

- 创建订单
- 修改订单状态
- 指派订单给管理员
- 不同角色可见订单范围不同：
    - **USER**：仅能查看自己的订单
    - **ADMIN_L2**：仅能查看自己负责的订单
    - **ADMIN_L1**：可查看所有订单

---

## 📊 订单统计模块

支持以下统计维度：

- 按周统计
- 按月统计
- 按年统计
- 历史统计（自订单创建以来）

统计内容包括：

- 订单数量
- 订单总金额

统计数据由后端聚合，前端直接用于图表展示。

---

## 📄 日志系统

### 日志框架

- 使用 **Logback**
- 配置文件：

```
src/main/resources/logback-spring.xml
```

### 日志输出目录

```
backend/logs/
```

### 记录内容

- 应用启动日志
- 接口访问日志
- SQL 执行日志
- 异常完整堆栈信息

日志目录在项目启动时会自动创建。

---

## ⚙️ 配置说明

### 数据库配置

配置文件：

```
src/main/resources/application-dev.yml
```

示例：

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/logistics?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

---

## ▶️ 启动方式

### 1️⃣ 创建数据库

```sql
CREATE DATABASE logistics DEFAULT CHARACTER SET utf8mb4;
```

### 2️⃣ 启动后端服务

```bash
cd backend
mvn spring-boot:run
```

访问地址：

```
http://localhost:8080
```

---

## 🔗 接口示例

### 登录接口

```
POST /auth/login
```

请求示例：

```json
{
  "username": "test",
  "password": "123456"
}
```

返回示例：

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

## 🎯 项目设计目标

- 清晰的三层结构（Controller / Service / Mapper）
- 职责分离，结构清楚
- 默认安全，避免权限漏洞
- 便于后续扩展：
    - 分页查询
    - 操作审计日志
    - 管理员后台
    - Docker / 部署优化

---

## 📌 说明

- 本项目用于 **学习展示**
- 侧重真实企业后端开发实践
- 避免无意义的过度设计

---
