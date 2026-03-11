package com.dk.logistics.module.system.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.system.permission.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    @Select("""
        SELECT DISTINCT p.permission_code
        FROM sys_permission p
        INNER JOIN sys_role_permission rp ON rp.permission_id = p.id
        INNER JOIN sys_user_role ur ON ur.role_id = rp.role_id
        WHERE ur.user_id = #{userId}
          AND p.deleted = 0
          AND p.status = 1
        """)
    List<String> selectPermissionCodesByUserId(Long userId);

    @Select("""
        SELECT id, permission_name, permission_code, api_uri, http_method,
               permission_type, status, remark, created_by, created_at,
               updated_by, updated_at, deleted
        FROM sys_permission
        WHERE permission_code = #{permissionCode}
          AND deleted = 0
        LIMIT 1
        """)
    SysPermission selectByPermissionCode(String permissionCode);

}