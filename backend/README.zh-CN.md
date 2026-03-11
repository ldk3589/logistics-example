# backend
[English](README.md) | [中文](README.zh-CN.md)

RBAC 权限管理系统的后端服务模块。

本模块基于 Spring Boot 开发，主要提供：

- 登录与注册
- 用户管理
- 角色管理
- 权限管理
- 菜单管理
- 部门管理
- 订单管理
- 登录日志与操作日志
- 基于 RBAC 的权限控制
- 订单数据范围控制

---

## 技术栈

- Java 21
- Spring Boot 3
- Spring Security
- JWT
- MyBatis-Plus
- MySQL
- Maven

---

## 模块结构

    backend                           # 后端文件夹
    ├── .mvn                          # Maven 工具
    ├── src                           # 源代码
    │   ├── main                      # 主要代码
    │   │   ├── java/com/dk/logistics # Java 应用代码
    │   │   │   ├── common            # 公共工具
    │   │   │   ├── config            # 应用设置
    │   │   │   ├── framework         # 基础框架
    │   │   │   └── module            # 业务模块
    │   │   │       ├── auth          # 登录和访问权限
    │   │   │       ├── order         # 订单模块
    │   │   │       └── system        # 系统模块
    │   │   │           ├── dept      # 部门模块
    │   │   │           ├── log       # 日志记录模块
    │   │   │           ├── menu      # 菜单模块
    │   │   │           ├── permission# 权限规则模块
    │   │   │           ├── role      # 用户角色模块
    │   │   │           └── user      # 用户模块
    │   │   └── resources             # 资源文件
    │   └── test                      # 测试代码
    ├── .gitattributes                # Git 规则
    ├── .gitignore                    # 隐藏不提交的文件
    ├── mvnw                          # Maven 工具 (Mac/Linux)
    ├── mvnw.cmd                      # Maven 工具 (Windows)
    ├── pom.xml                       # 工具列表
    ├── README.md                     # 说明文件 (英文)
    └── README.zh-CN.md               # 说明文件 (中文)

---

## 主要功能

### auth

- 注册
- 登录
- 返回当前用户信息
- 返回当前用户菜单
- 返回当前用户权限

### system/user

- 用户列表
- 用户详情
- 新增用户
- 修改用户
- 删除用户
- 修改用户状态
- 给用户分配角色

### system/role

- 角色列表
- 角色详情
- 新增角色
- 修改角色
- 删除角色
- 分配权限
- 分配菜单

### system/permission

- 权限列表
- 权限详情
- 新增权限
- 修改权限
- 删除权限

### system/menu

- 菜单树列表
- 菜单详情
- 新增菜单
- 修改菜单
- 删除菜单

### system/dept

- 部门树列表
- 部门详情
- 新增部门
- 修改部门
- 删除部门

### system/log

- 查询登录日志
- 查询操作日志

### order

- 订单列表
- 新增订单
- 修改订单
- 删除订单
- 分配订单
- 完成订单

---

## RBAC 设计

系统采用标准 RBAC 模型：

- 用户 -> 角色
- 角色 -> 权限
- 角色 -> 菜单

说明：

- 用户不直接拥有权限
- 用户通过角色获得权限
- 后端权限校验才是真正的安全边界
- 前端是否显示按钮只是为了更好的使用体验

---

## 数据权限

订单模块支持数据范围控制。

常见数据范围包括：

- ALL
- DEPT
- DEPT_AND_CHILD
- SELF

这表示不同角色能看到不同范围的订单数据。

---

## 数据表

常见表包括：

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

## 启动后端

### 1. 配置数据库

请修改后端资源文件中的数据库配置。

常见配置项：

    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/your_database_name
        username: root
        password: your_password

### 2. 启动服务

    cd backend
    mvn spring-boot:run

默认地址：

    http://localhost:8080

---

## 接口风格

后端采用：

- RESTful API
- 统一返回结构
- 全局异常处理
- JWT 鉴权
- 方法级权限控制

---

## 使用说明

在测试前，请先确认：

- 数据库表已经初始化
- 测试角色和权限数据已经插入
- 用户密码已经正确加密
- 数据库菜单路径和前端路由路径一致

如果登录失败，优先检查：

- 数据库连接
- 密码加密
- 角色 / 权限初始化数据
- JWT 配置

---

## 后续优化方向

- 完善 Swagger 文档
- Docker 部署
- 增加单元测试
- 导出功能
- 更完善的数据权限设计