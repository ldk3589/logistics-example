# frontend
[English](README.md) | [中文](README.zh-CN.md)

RBAC 权限管理系统的前端模块。

本模块基于 Vue 3 和 Element Plus 开发。

主要包含这些页面：

- 登录与注册
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

## 技术栈

- Vue 3
- Vite
- Vue Router
- Element Plus
- Axios

---

## 项目结构

    frontend                          # 前端文件夹
    ├── public                        # 公共文件
    ├── src                           # 源代码
    │   ├── layout                    # 页面布局
    │   ├── router                    # 页面链接
    │   ├── utils                     # 帮助工具
    │   ├── views                     # 页面视图
    │   ├── App.vue                   # 主 Vue 页面
    │   └── main.js                   # 主 JS 文件
    ├── .gitattributes                # Git 规则
    ├── .gitignore                    # 隐藏不提交的文件
    ├── index.html                    # 主网页
    ├── package.json                  # 工具列表
    ├── package-lock.json             # 锁定工具
    ├── vite.config.js                # Vite 设置
    ├── README.md                     # 说明文件 (英文)
    └── README.zh-CN.md               # 说明文件 (中文)

---

## 主要页面

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

## 前端功能

### 认证功能

- 登录页
- 注册页
- token 保存
- 路由守卫
- 退出登录

### 请求处理

- Axios 请求拦截器
- 自动携带 JWT token
- 统一错误提示
- token 失效时跳回登录页

### 权限展示

- 动态菜单显示
- 按钮根据权限码显示
- 页面访问受登录状态控制

### 业务页面

- 用户管理相关页面
- 角色管理相关页面
- 权限管理相关页面
- 菜单管理相关页面
- 部门管理相关页面
- 订单管理相关页面
- 登录日志和操作日志页面

---

## 启动前端

    cd frontend
    npm install
    npm run dev

默认地址：

    http://localhost:5173

---

## 代理配置

前端开发时通常会把请求代理到后端服务。

本地开发常见目标地址：

    http://localhost:8080

请检查：

    vite.config.js

---

## 路由说明

前端路由路径必须和后端返回的菜单路径一致。

例如，如果后端菜单 path 是：

    /system/user
    /system/role
    /system/menu

那前端 router 也必须支持这些路径。

如果页面是空白的，优先检查：

- 前端路由是否存在
- 后端菜单 path 是否和 router 一致
- 当前用户是否有权限
- 接口路径是否正确

---

## 权限说明

前端按钮是否显示，取决于权限码。

例如：

- sys:user:add
- sys:user:update
- sys:user:delete
- sys:role:add
- sys:permission:update
- order:assign

但真正的安全控制仍然后端权限校验为准。

---

## 后续优化方向

- 动态路由注册
- 可复用的权限指令
- 可复用的表格和表单组件
- 更好的部门选择器
- 更好的订单负责人选择组件
- 导入导出功能