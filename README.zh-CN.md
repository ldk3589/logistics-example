# logistics-example

[English](README.md) | [中文](READM.zh-CN.md)

一个基于 Spring Boot 和 Vue 3 的前后端分离 RBAC 权限管理系统。

本项目主要面向企业后台常见场景，包含：

- 登录与注册
- 用户、角色、权限、菜单、部门管理
- 订单业务模块
- 登录日志与操作日志
- 基于角色的访问控制
- 前端动态菜单展示

适合作为：

- Java 后端练手项目
- 前后端联调练习项目
- 简历项目 / 面试项目

---

## 项目功能

### 认证功能

- 用户注册
- 用户登录
- JWT Token 认证
- 前端路由守卫
- Axios 请求拦截

### RBAC 权限管理

- 用户管理
- 角色管理
- 权限管理
- 菜单管理
- 部门管理
- 用户分配角色
- 角色分配权限
- 角色分配菜单

### 业务模块

- 订单列表
- 新增订单
- 分配订单
- 完成订单
- 删除订单
- 基于角色与数据范围控制数据可见性

### 日志功能

- 登录日志查询
- 操作日志查询

---

## 技术栈

### 后端

- Java 21
- Spring Boot 3
- Spring Security
- JWT
- MyBatis-Plus
- MySQL
- Maven

### 前端

- Vue 3
- Vite
- Vue Router
- Element Plus
- Axios

---

## 项目结构

    logistics-example             # 主文件夹
    ├── backend                   # 后端部分 (Java)
    │   ├── .mvn                  # Maven 工具文件
    │   ├── src                   # 源代码文件夹
    │   │   ├── main              # 主要代码
    │   │   │   ├── java          # Java 代码
    │   │   │   │   └── com
    │   │   │   │       └── dk
    │   │   │   │           └── logistics # 你的应用代码
    │   │   │   └── resources     # 应用设置和数据
    │   │   └── test              # 测试代码
    │   ├── .gitattributes        # Git 设置
    │   ├── .gitignore            # 隐藏不提交的文件
    │   ├── mvnw                  # Maven 工具 (Mac/Linux)
    │   ├── mvnw.cmd              # Maven 工具 (Windows)
    │   ├── pom.xml               # 后端工具列表
    │   └── README.zh-CN.md       # 说明文件 (中文)
    │
    ├── frontend                  # 前端部分 (Vue)
    │   ├── public                # 公共文件
    │   ├── src                   # 源代码文件夹
    │   │   ├── layout            # 页面布局
    │   │   ├── router            # 页面链接
    │   │   ├── utils             # 帮助工具
    │   │   ├── views             # 页面视图
    │   │   ├── App.vue           # 主 Vue 页面
    │   │   └── main.js           # 主 JS 文件
    │   ├── .gitattributes        # Git 设置
    │   ├── .gitignore            # 隐藏不提交的文件
    │   ├── index.html            # 主网页
    │   ├── package.json          # 前端工具列表
    │   ├── package-lock.json     # 锁定工具版本
    │   ├── vite.config.js        # Vite 工具设置
    │   ├── README.md             # 说明文件 (英文)
    │   └── README.zh-CN.md       # 说明文件 (中文)
    │
    ├── .gitignore                # 隐藏不提交的文件
    ├── README.md                 # 说明文件 (英文)
    └── README-zh.md              # 说明文件 (中文)
---

## 后端模块说明

- auth
    - 登录
    - 注册
    - 当前用户菜单

- system/user
    - 用户增删改查
    - 分配角色

- system/role
    - 角色增删改查
    - 分配权限
    - 分配菜单

- system/permission
    - 权限增删改查

- system/menu
    - 菜单增删改查
    - 菜单树

- system/dept
    - 部门树
    - 部门增删改查

- system/log
    - 登录日志
    - 操作日志

- order
    - 订单增删改查
    - 分配 / 完成订单
    - 数据范围过滤

---

## 前端页面

- 登录页
- 首页
- 用户管理
- 角色管理
- 权限管理
- 菜单管理
- 部门管理
- 订单管理
- 登录日志
- 操作日志

---

## 快速启动

### 1. 克隆项目

    git clone https://github.com/ldk3589/logistics-example.git
    cd logistics-example

### 2. 启动后端

    cd backend
    mvn spring-boot:run

后端默认地址：

    http://localhost:8080

### 3. 启动前端

    cd frontend
    npm install
    npm run dev

前端默认地址：

    http://localhost:5173

---

## 数据库说明

请先准备好 MySQL 数据库，然后在后端配置文件中修改数据库连接信息。

常见需要配置的内容包括：

- 数据库地址
- 端口
- 数据库名
- 用户名
- 密码

同时还需要初始化：

- 用户表
- 角色表
- 权限表
- 菜单表
- 部门表
- 用户-角色关联表
- 角色-权限关联表
- 角色-菜单关联表
- 订单表
- 登录日志表
- 操作日志表

---

## 权限设计

系统采用典型的 RBAC 模型：

- 用户 -> 角色
- 角色 -> 权限
- 角色 -> 菜单

在本项目中：

- 用户不直接拥有权限
- 用户通过角色获得权限
- 前端按钮是否显示依赖权限码
- 真正的安全控制仍以后端权限校验为准

---

## 使用说明

在测试项目前，请先确认：

- 数据库中的菜单路径与前端路由路径一致
- 角色和权限初始化数据正确
- 用户密码加密正确
- 后端接口地址与前端请求地址一致

如果某些页面是空白的，优先检查：

- 前端路由路径
- 数据库菜单 path
- 当前用户是否有权限
- 后端接口地址是否正确

---

## 后续可优化方向

- 动态路由注册
- 数据导出
- Swagger 文档完善
- Docker 部署
- 单元测试
- 页面级、按钮级权限进一步封装
- 更好的部门选择器和用户选择器组件

---

## License

本项目用于学习和个人展示。