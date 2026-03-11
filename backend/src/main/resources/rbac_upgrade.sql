SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- 1. 部门表
-- =========================
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept (
                          id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
                          parent_id       BIGINT NOT NULL DEFAULT 0 COMMENT '父部门ID，0表示根部门',
                          dept_name       VARCHAR(100) NOT NULL COMMENT '部门名称',
                          dept_code       VARCHAR(100) DEFAULT NULL COMMENT '部门编码',
                          leader_user_id  BIGINT DEFAULT NULL COMMENT '负责人用户ID',
                          sort_num        INT NOT NULL DEFAULT 0 COMMENT '排序',
                          status          TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
                          remark          VARCHAR(255) DEFAULT NULL COMMENT '备注',
                          created_by      BIGINT DEFAULT NULL COMMENT '创建人',
                          created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          updated_by      BIGINT DEFAULT NULL COMMENT '修改人',
                          updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                          deleted         TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
                          INDEX idx_parent_id(parent_id),
                          INDEX idx_leader_user_id(leader_user_id),
                          INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';


-- =========================
-- 2. 用户表
-- =========================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
                          id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
                          username        VARCHAR(50) NOT NULL COMMENT '用户名',
                          password        VARCHAR(255) NOT NULL COMMENT '密码（加密）',
                          nickname        VARCHAR(100) NOT NULL COMMENT '昵称',
                          real_name       VARCHAR(100) DEFAULT NULL COMMENT '真实姓名',
                          phone           VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                          email           VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                          avatar          VARCHAR(255) DEFAULT NULL COMMENT '头像',
                          dept_id         BIGINT DEFAULT NULL COMMENT '部门ID',
                          status          TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
                          is_admin        TINYINT NOT NULL DEFAULT 0 COMMENT '是否超级管理员：1是，0否',
                          last_login_ip   VARCHAR(64) DEFAULT NULL COMMENT '最后登录IP',
                          last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
                          remark          VARCHAR(255) DEFAULT NULL COMMENT '备注',
                          created_by      BIGINT DEFAULT NULL COMMENT '创建人',
                          created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          updated_by      BIGINT DEFAULT NULL COMMENT '修改人',
                          updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                          deleted         TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
                          UNIQUE KEY uk_username(username),
                          UNIQUE KEY uk_phone(phone),
                          UNIQUE KEY uk_email(email),
                          INDEX idx_dept_id(dept_id),
                          INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


-- =========================
-- 3. 角色表
-- =========================
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
                          id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
                          role_name       VARCHAR(100) NOT NULL COMMENT '角色名称',
                          role_code       VARCHAR(100) NOT NULL COMMENT '角色编码，如ADMIN、ORDER_MANAGER',
                          data_scope      VARCHAR(30) NOT NULL DEFAULT 'SELF' COMMENT '数据权限范围：ALL/DEPT/DEPT_AND_CHILD/SELF/CUSTOM',
                          status          TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
                          sort_num        INT NOT NULL DEFAULT 0 COMMENT '排序',
                          remark          VARCHAR(255) DEFAULT NULL COMMENT '备注',
                          created_by      BIGINT DEFAULT NULL COMMENT '创建人',
                          created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          updated_by      BIGINT DEFAULT NULL COMMENT '修改人',
                          updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                          deleted         TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
                          UNIQUE KEY uk_role_code(role_code),
                          INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';


-- =========================
-- 4. 权限表
-- =========================
DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission (
                                id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
                                permission_name     VARCHAR(100) NOT NULL COMMENT '权限名称',
                                permission_code     VARCHAR(100) NOT NULL COMMENT '权限编码，如sys:user:list',
                                api_uri             VARCHAR(255) DEFAULT NULL COMMENT '接口路径',
                                http_method         VARCHAR(20) DEFAULT NULL COMMENT '请求方法：GET/POST/PUT/DELETE',
                                permission_type     VARCHAR(20) NOT NULL DEFAULT 'API' COMMENT '权限类型：API/BUTTON',
                                status              TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
                                remark              VARCHAR(255) DEFAULT NULL COMMENT '备注',
                                created_by          BIGINT DEFAULT NULL COMMENT '创建人',
                                created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                updated_by          BIGINT DEFAULT NULL COMMENT '修改人',
                                updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                deleted             TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
                                UNIQUE KEY uk_permission_code(permission_code),
                                INDEX idx_api_uri(api_uri),
                                INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';


-- =========================
-- 5. 菜单表
-- =========================
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
                          id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
                          parent_id           BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID，0表示根菜单',
                          menu_name           VARCHAR(100) NOT NULL COMMENT '菜单名称',
                          menu_type           VARCHAR(20) NOT NULL COMMENT '菜单类型：DIR/MENU/BUTTON',
                          path                VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
                          component           VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
                          route_name          VARCHAR(100) DEFAULT NULL COMMENT '路由名称',
                          icon                VARCHAR(100) DEFAULT NULL COMMENT '图标',
                          permission_code     VARCHAR(100) DEFAULT NULL COMMENT '菜单或按钮对应权限编码',
                          visible             TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示：1显示，0隐藏',
                          keep_alive          TINYINT NOT NULL DEFAULT 0 COMMENT '是否缓存：1是，0否',
                          sort_num            INT NOT NULL DEFAULT 0 COMMENT '排序',
                          status              TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
                          remark              VARCHAR(255) DEFAULT NULL COMMENT '备注',
                          created_by          BIGINT DEFAULT NULL COMMENT '创建人',
                          created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          updated_by          BIGINT DEFAULT NULL COMMENT '修改人',
                          updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                          deleted             TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
                          INDEX idx_parent_id(parent_id),
                          INDEX idx_menu_type(menu_type),
                          INDEX idx_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';


-- =========================
-- 6. 用户角色关联表
-- =========================
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
                               id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                               user_id         BIGINT NOT NULL COMMENT '用户ID',
                               role_id         BIGINT NOT NULL COMMENT '角色ID',
                               created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               UNIQUE KEY uk_user_role(user_id, role_id),
                               INDEX idx_user_id(user_id),
                               INDEX idx_role_id(role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';


-- =========================
-- 7. 角色权限关联表
-- =========================
DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission (
                                     id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                     role_id         BIGINT NOT NULL COMMENT '角色ID',
                                     permission_id   BIGINT NOT NULL COMMENT '权限ID',
                                     created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     UNIQUE KEY uk_role_permission(role_id, permission_id),
                                     INDEX idx_role_id(role_id),
                                     INDEX idx_permission_id(permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';


-- =========================
-- 8. 角色菜单关联表
-- =========================
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
                               id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                               role_id         BIGINT NOT NULL COMMENT '角色ID',
                               menu_id         BIGINT NOT NULL COMMENT '菜单ID',
                               created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               UNIQUE KEY uk_role_menu(role_id, menu_id),
                               INDEX idx_role_id(role_id),
                               INDEX idx_menu_id(menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';


-- =========================
-- 9. 角色可访问部门表（给 CUSTOM 数据权限用）
-- =========================
DROP TABLE IF EXISTS sys_role_dept;
CREATE TABLE sys_role_dept (
                               id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                               role_id         BIGINT NOT NULL COMMENT '角色ID',
                               dept_id         BIGINT NOT NULL COMMENT '部门ID',
                               created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               UNIQUE KEY uk_role_dept(role_id, dept_id),
                               INDEX idx_role_id(role_id),
                               INDEX idx_dept_id(dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色部门关联表';


-- =========================
-- 10. 登录日志表
-- =========================
DROP TABLE IF EXISTS sys_login_log;
CREATE TABLE sys_login_log (
                               id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
                               username        VARCHAR(50) DEFAULT NULL COMMENT '用户名',
                               login_status    TINYINT NOT NULL COMMENT '登录状态：1成功，0失败',
                               login_ip        VARCHAR(64) DEFAULT NULL COMMENT '登录IP',
                               login_location  VARCHAR(255) DEFAULT NULL COMMENT '登录地点',
                               browser         VARCHAR(100) DEFAULT NULL COMMENT '浏览器',
                               os              VARCHAR(100) DEFAULT NULL COMMENT '操作系统',
                               message         VARCHAR(255) DEFAULT NULL COMMENT '提示消息',
                               created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
                               INDEX idx_username(username),
                               INDEX idx_login_status(login_status),
                               INDEX idx_created_at(created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';


-- =========================
-- 11. 操作日志表
-- =========================
DROP TABLE IF EXISTS sys_operation_log;
CREATE TABLE sys_operation_log (
                                   id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
                                   module_name         VARCHAR(100) DEFAULT NULL COMMENT '模块名称',
                                   operation_name      VARCHAR(100) DEFAULT NULL COMMENT '操作名称',
                                   request_method      VARCHAR(20) DEFAULT NULL COMMENT '请求方法',
                                   request_uri         VARCHAR(255) DEFAULT NULL COMMENT '请求地址',
                                   operator_user_id    BIGINT DEFAULT NULL COMMENT '操作人ID',
                                   operator_name       VARCHAR(100) DEFAULT NULL COMMENT '操作人名称',
                                   operator_ip         VARCHAR(64) DEFAULT NULL COMMENT '操作IP',
                                   request_params      TEXT COMMENT '请求参数',
                                   response_data       TEXT COMMENT '返回结果',
                                   operation_status    TINYINT NOT NULL DEFAULT 1 COMMENT '操作状态：1成功，0失败',
                                   error_message       TEXT COMMENT '错误信息',
                                   cost_time_ms        BIGINT DEFAULT NULL COMMENT '耗时毫秒',
                                   created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                   INDEX idx_operator_user_id(operator_user_id),
                                   INDEX idx_operation_status(operation_status),
                                   INDEX idx_created_at(created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';


-- =========================
-- 12. 业务表：订单表（升级版）
-- =========================
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
                        id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
                        order_no            VARCHAR(64) NOT NULL COMMENT '订单号',
                        customer_name       VARCHAR(100) NOT NULL COMMENT '客户名称',
                        customer_phone      VARCHAR(20) DEFAULT NULL COMMENT '客户电话',
                        source_address      VARCHAR(255) DEFAULT NULL COMMENT '发货地址',
                        target_address      VARCHAR(255) DEFAULT NULL COMMENT '收货地址',
                        status              VARCHAR(30) NOT NULL DEFAULT 'CREATED' COMMENT '订单状态',
                        amount              DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
                        dept_id             BIGINT DEFAULT NULL COMMENT '所属部门ID',
                        owner_user_id       BIGINT DEFAULT NULL COMMENT '负责人ID',
                        created_by          BIGINT DEFAULT NULL COMMENT '创建人',
                        updated_by          BIGINT DEFAULT NULL COMMENT '修改人',
                        created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                        deleted             TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
                        UNIQUE KEY uk_order_no(order_no),
                        INDEX idx_status(status),
                        INDEX idx_dept_id(dept_id),
                        INDEX idx_owner_user_id(owner_user_id),
                        INDEX idx_created_by(created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


-- =========================
-- 13. 订单审核日志表
-- =========================
DROP TABLE IF EXISTS order_audit_log;
CREATE TABLE order_audit_log (
                                 id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
                                 order_id            BIGINT NOT NULL COMMENT '订单ID',
                                 action_type         VARCHAR(50) NOT NULL COMMENT '操作类型：CREATE/ASSIGN/APPROVE/REJECT/FINISH',
                                 operator_user_id    BIGINT DEFAULT NULL COMMENT '操作人ID',
                                 operator_name       VARCHAR(100) DEFAULT NULL COMMENT '操作人名称',
                                 before_status       VARCHAR(30) DEFAULT NULL COMMENT '操作前状态',
                                 after_status        VARCHAR(30) DEFAULT NULL COMMENT '操作后状态',
                                 remark              VARCHAR(255) DEFAULT NULL COMMENT '备注',
                                 created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                 INDEX idx_order_id(order_id),
                                 INDEX idx_operator_user_id(operator_user_id),
                                 INDEX idx_created_at(created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单审核日志表';


-- =========================
-- 14. 初始化部门数据
-- =========================
INSERT INTO sys_dept (id, parent_id, dept_name, dept_code, sort_num, status, remark)
VALUES
    (1, 0, '总部', 'HEAD', 1, 1, '根部门'),
    (2, 1, '运营部', 'OPS', 2, 1, '运营部门'),
    (3, 1, '仓储部', 'WAREHOUSE', 3, 1, '仓储部门'),
    (4, 1, '财务部', 'FINANCE', 4, 1, '财务部门');


-- =========================
-- 15. 初始化角色
-- =========================
INSERT INTO sys_role (id, role_name, role_code, data_scope, status, sort_num, remark)
VALUES
    (1, '超级管理员', 'SUPER_ADMIN', 'ALL', 1, 1, '拥有全部权限'),
    (2, '部门管理员', 'DEPT_ADMIN', 'DEPT_AND_CHILD', 1, 2, '可管理本部门及子部门数据'),
    (3, '订单专员', 'ORDER_USER', 'SELF', 1, 3, '仅可查看自己数据'),
    (4, '审计人员', 'AUDITOR', 'ALL', 1, 4, '查看日志与审计信息');


-- =========================
-- 16. 初始化权限
-- =========================
INSERT INTO sys_permission (id, permission_name, permission_code, api_uri, http_method, permission_type, status, remark)
VALUES
    (1, '用户列表', 'sys:user:list', '/api/users', 'GET', 'API', 1, '查看用户列表'),
    (2, '新增用户', 'sys:user:add', '/api/users', 'POST', 'API', 1, '新增用户'),
    (3, '修改用户', 'sys:user:update', '/api/users/*', 'PUT', 'API', 1, '修改用户'),
    (4, '删除用户', 'sys:user:delete', '/api/users/*', 'DELETE', 'API', 1, '删除用户'),

    (5, '角色列表', 'sys:role:list', '/api/roles', 'GET', 'API', 1, '查看角色列表'),
    (6, '角色新增', 'sys:role:add', '/api/roles', 'POST', 'API', 1, '新增角色'),
    (7, '角色修改', 'sys:role:update', '/api/roles/*', 'PUT', 'API', 1, '修改角色'),
    (8, '角色删除', 'sys:role:delete', '/api/roles/*', 'DELETE', 'API', 1, '删除角色'),

    (9, '菜单列表', 'sys:menu:list', '/api/menus', 'GET', 'API', 1, '查看菜单列表'),
    (10, '菜单新增', 'sys:menu:add', '/api/menus', 'POST', 'API', 1, '新增菜单'),
    (11, '菜单修改', 'sys:menu:update', '/api/menus/*', 'PUT', 'API', 1, '修改菜单'),
    (12, '菜单删除', 'sys:menu:delete', '/api/menus/*', 'DELETE', 'API', 1, '删除菜单'),

    (13, '权限列表', 'sys:permission:list', '/api/permissions', 'GET', 'API', 1, '查看权限列表'),
    (14, '权限新增', 'sys:permission:add', '/api/permissions', 'POST', 'API', 1, '新增权限'),
    (15, '权限修改', 'sys:permission:update', '/api/permissions/*', 'PUT', 'API', 1, '修改权限'),
    (16, '权限删除', 'sys:permission:delete', '/api/permissions/*', 'DELETE', 'API', 1, '删除权限'),

    (17, '部门列表', 'sys:dept:list', '/api/depts', 'GET', 'API', 1, '查看部门列表'),
    (18, '部门新增', 'sys:dept:add', '/api/depts', 'POST', 'API', 1, '新增部门'),
    (19, '部门修改', 'sys:dept:update', '/api/depts/*', 'PUT', 'API', 1, '修改部门'),
    (20, '部门删除', 'sys:dept:delete', '/api/depts/*', 'DELETE', 'API', 1, '删除部门'),

    (21, '订单列表', 'order:list', '/api/orders', 'GET', 'API', 1, '查看订单'),
    (22, '订单新增', 'order:add', '/api/orders', 'POST', 'API', 1, '新增订单'),
    (23, '订单修改', 'order:update', '/api/orders/*', 'PUT', 'API', 1, '修改订单'),
    (24, '订单删除', 'order:delete', '/api/orders/*', 'DELETE', 'API', 1, '删除订单'),
    (25, '订单分配', 'order:assign', '/api/orders/*/assign', 'POST', 'API', 1, '订单分配'),

    (26, '登录日志查看', 'sys:loginlog:list', '/api/logins', 'GET', 'API', 1, '查看登录日志'),
    (27, '操作日志查看', 'sys:oplog:list', '/api/operations', 'GET', 'API', 1, '查看操作日志');


-- =========================
-- 17. 初始化菜单
-- =========================
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, route_name, icon, permission_code, visible, keep_alive, sort_num, status, remark)
VALUES
    (1, 0, '系统管理', 'DIR', '/system', 'Layout', 'System', 'Setting', NULL, 1, 1, 1, 1, '系统管理目录'),
    (2, 1, '用户管理', 'MENU', '/system/user', 'system/user/index', 'UserManage', 'User', 'sys:user:list', 1, 1, 1, 1, '用户管理'),
    (3, 1, '角色管理', 'MENU', '/system/role', 'system/role/index', 'RoleManage', 'Avatar', 'sys:role:list', 1, 1, 2, 1, '角色管理'),
    (4, 1, '菜单管理', 'MENU', '/system/menu', 'system/menu/index', 'MenuManage', 'Menu', 'sys:menu:list', 1, 1, 3, 1, '菜单管理'),
    (5, 1, '权限管理', 'MENU', '/system/permission', 'system/permission/index', 'PermissionManage', 'Lock', 'sys:permission:list', 1, 1, 4, 1, '权限管理'),
    (6, 1, '部门管理', 'MENU', '/system/dept', 'system/dept/index', 'DeptManage', 'OfficeBuilding', 'sys:dept:list', 1, 1, 5, 1, '部门管理'),

    (7, 0, '业务管理', 'DIR', '/business', 'Layout', 'Business', 'Suitcase', NULL, 1, 1, 2, 1, '业务管理目录'),
    (8, 7, '订单管理', 'MENU', '/business/order', 'business/order/index', 'OrderManage', 'Document', 'order:list', 1, 1, 1, 1, '订单管理'),

    (9, 0, '日志管理', 'DIR', '/log', 'Layout', 'Log', 'Notebook', NULL, 1, 1, 3, 1, '日志管理目录'),
    (10, 9, '登录日志', 'MENU', '/log/login', 'log/login/index', 'LoginLog', 'Histogram', 'sys:loginlog:list', 1, 1, 1, 1, '登录日志'),
    (11, 9, '操作日志', 'MENU', '/log/operation', 'log/operation/index', 'OperationLog', 'Tickets', 'sys:oplog:list', 1, 1, 2, 1, '操作日志');


-- =========================
-- 18. 初始化用户
-- 密码这里先假设是 BCrypt 加密后的 admin123 / dept123 / user123 / audit123
-- 你后面可在项目启动时重新生成
-- =========================
INSERT INTO sys_user (id, username, password, nickname, real_name, phone, email, dept_id, status, is_admin, remark)
VALUES
    (1, 'admin', '$2a$10$7QJqV0GQxj0O7V8R4xqY1u0d4cN3I5J8zS2XwWz2zV9kD9xFfJk2G', '超级管理员', '系统管理员', '13800000001', 'admin@example.com', 1, 1, 1, '系统初始化管理员'),
    (2, 'deptadmin', '$2a$10$7QJqV0GQxj0O7V8R4xqY1u0d4cN3I5J8zS2XwWz2zV9kD9xFfJk2G', '部门管理员', '运营主管', '13800000002', 'deptadmin@example.com', 2, 1, 0, '部门管理员'),
    (3, 'user1', '$2a$10$7QJqV0GQxj0O7V8R4xqY1u0d4cN3I5J8zS2XwWz2zV9kD9xFfJk2G', '订单专员', '张三', '13800000003', 'user1@example.com', 2, 1, 0, '普通业务用户'),
    (4, 'auditor', '$2a$10$7QJqV0GQxj0O7V8R4xqY1u0d4cN3I5J8zS2XwWz2zV9kD9xFfJk2G', '审计员', '李四', '13800000004', 'auditor@example.com', 4, 1, 0, '审计用户');


-- =========================
-- 19. 初始化用户角色关系
-- =========================
INSERT INTO sys_user_role (user_id, role_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4);


-- =========================
-- 20. 初始化角色权限关系
-- 超级管理员给全部权限
-- =========================
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

-- 部门管理员权限
INSERT INTO sys_role_permission (role_id, permission_id)
VALUES
    (2, 1),(2,2),(2,3),
    (2,5),(2,9),(2,17),
    (2,21),(2,22),(2,23),(2,25),
    (2,26),(2,27);

-- 订单专员权限
INSERT INTO sys_role_permission (role_id, permission_id)
VALUES
    (3,21),(3,22),(3,23);

-- 审计人员权限
INSERT INTO sys_role_permission (role_id, permission_id)
VALUES
    (4,26),(4,27),(4,21);


-- =========================
-- 21. 初始化角色菜单关系
-- 超级管理员给全部菜单
-- =========================
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu;

-- 部门管理员菜单
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES
    (2,1),(2,2),(2,6),
    (2,7),(2,8),
    (2,9),(2,10),(2,11);

-- 订单专员菜单
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES
    (3,7),(3,8);

-- 审计人员菜单
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES
    (4,7),(4,8),(4,9),(4,10),(4,11);


-- =========================
-- 22. 初始化角色部门范围（CUSTOM 时才需要，这里先演示）
-- =========================
INSERT INTO sys_role_dept (role_id, dept_id)
VALUES
    (2, 2),
    (2, 3);


-- =========================
-- 23. 初始化订单测试数据
-- =========================
INSERT INTO orders (order_no, customer_name, customer_phone, source_address, target_address, status, amount, dept_id, owner_user_id, created_by, updated_by)
VALUES
    ('ORD202603090001', '客户A', '13911110001', '北京海淀区', '上海浦东新区', 'CREATED', 1200.00, 2, 3, 3, 3),
    ('ORD202603090002', '客户B', '13911110002', '广州天河区', '深圳南山区', 'ASSIGNED', 3600.00, 2, 2, 2, 2),
    ('ORD202603090003', '客户C', '13911110003', '成都武侯区', '重庆渝北区', 'FINISHED', 5200.00, 3, 2, 2, 2);

SET FOREIGN_KEY_CHECKS = 1;