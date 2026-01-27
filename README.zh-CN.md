# logistics-example
[English](README.md) | [中文](README.zh-CN.md)

🚚 Logistics Example

一个 **前后端分离的物流订单管理系统**，支持用户注册 / 登录、订单创建、订单状态管理、订单统计与权限分级（普通用户 / 管理员）。

本项目为 **学习导向型项目**，适合用于  
**Spring Boot + Vue 3 + MyBatis-Plus + Element Plus** 的全栈综合练手，尤其适合日企 / Java 后端 / 全栈岗位展示。

---

## 📁 项目结构

```
logistics-example
├── backend/                # 后端：Spring Boot 项目（logistics-backend）
│   ├── src/
│   ├── logs/               # 后端运行日志（自动生成）
│   ├── pom.xml
│   └── README.md           # 后端子项目说明
│
├── frontend/               # 前端：Vue 3 + Vite 项目（logistics-frontend）
│   ├── src/
│   ├── logs/               # 前端日志（可选）
│   ├── package.json
│   └── README.md           # 前端子项目说明
│
├── .gitattributes
└── README.md               # 项目总说明（当前文件）
```

---

## 🚀 功能介绍

### ✅ 已实现功能

#### 👤 用户与权限
- 用户注册 / 登录
- 自动去除用户名、密码首尾空格
- 禁止用户名 / 密码中间包含空格
- 普通用户 / 管理员权限区分
- 管理员密码校验（一级 / 二级管理员）

#### 📦 订单管理
- 创建订单
- 订单状态流转（PROCESSING / COMPLETED / CANCELLED）
- 支持金额、联系方式、通知方式
- 按权限查看订单数据

#### 📊 数据统计
- 订单数量统计
- 订单金额统计
- 支持 **周 / 月 / 年 / 历史** 维度
- ECharts 折线趋势图展示

#### 🔐 安全与工程化
- JWT 登录认证
- Axios 请求拦截器
- 未登录自动跳转登录页
- 后端统一异常处理
- 前后端日志自动记录

---

## 🧱 技术栈

### 后端（Backend）
- Java 21
- Spring Boot
- Spring Security（JWT）
- MyBatis-Plus
- MySQL
- Maven
- Logback（文件日志）

### 前端（Frontend）
- Vue 3
- Vite
- Element Plus
- Axios
- Vue Router
- ECharts

---

## ⚙️ 环境准备

### 必须环境

- JDK ≥ 17（推荐 21）
- Node.js ≥ 18
- MySQL ≥ 8.0
- Maven ≥ 3.8

---

## 🛠️ 快速启动

### 1️⃣ 后端启动（Spring Boot）

#### ① 创建数据库

```sql
CREATE DATABASE logistics DEFAULT CHARACTER SET utf8mb4;
```

#### ② 修改数据库配置

文件路径：

```
backend/src/main/resources/application-dev.yml
```

示例：

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/logistics?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

#### ③ 启动后端

```bash
cd backend
mvn spring-boot:run
```

启动成功后访问：

```
http://localhost:8080
```

---

### 2️⃣ 前端启动（Vue 3）

```bash
cd frontend
npm install
npm run dev
```

浏览器访问：

```
http://localhost:5173
```

---

## 🔗 接口示例（节选）

### 用户登录

```
POST /auth/login
```

请求参数：

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

## 🔐 登录与注册逻辑说明

- 用户注册：
    - 自动校验用户名 / 密码合法性
    - 默认注册为普通用户
    - 管理员注册需输入正确的管理员密码

- 用户登录：
    - 登录成功返回 Token + 用户信息
    - Token 存储于 localStorage
    - 前端路由守卫控制访问权限

---

## 📄 日志说明

### 后端日志
- 自动生成于：

```
backend/logs/
```

- 包含：
    - 启动日志
    - SQL 执行日志
    - 接口访问与异常日志

### 前端日志
- 可扩展为：
    - 控制台错误捕获
    - 请求失败日志
    - 用户行为日志

---

## 🎯 项目定位

- ✔ Java 后端 / 全栈面试项目
- ✔ Spring Boot + Vue 3 综合实战
- ✔ 可持续扩展为真实业务系统
- ✔ 结构清晰，适合二次开发

---

## 📌 后续可扩展方向（建议）

- 订单分页与搜索
- 管理员后台面板
- 数据导出（Excel）
- 操作审计日志
- Docker 一键部署

---
