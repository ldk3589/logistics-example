package com.dk.logistics.module.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.system.role.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {
    @Select("""
    SELECT permission_id
    FROM sys_role_permission
    WHERE role_id = #{roleId}
    """)
    List<Long> selectPermissionIdsByRoleId(Long roleId);
    @Select("""
        SELECT COUNT(1)
        FROM sys_role_permission
        WHERE permission_id = #{permissionId}
        """)
    Long countByPermissionId(Long permissionId);
}