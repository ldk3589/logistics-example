package com.dk.logistics.module.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.system.role.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("""
        SELECT r.role_code
        FROM sys_role r
        INNER JOIN sys_user_role ur ON ur.role_id = r.id
        WHERE ur.user_id = #{userId}
          AND r.deleted = 0
          AND r.status = 1
        """)
    List<String> selectRoleCodesByUserId(Long userId);

    @Select("""
        SELECT id, role_name, role_code, data_scope, status, sort_num, remark,
               created_by, created_at, updated_by, updated_at, deleted
        FROM sys_role
        WHERE role_code = #{roleCode}
          AND deleted = 0
        LIMIT 1
        """)
    SysRole selectByRoleCode(String roleCode);

    @Select("""
    SELECT data_scope
    FROM sys_role r
    INNER JOIN sys_user_role ur ON ur.role_id = r.id
    WHERE ur.user_id = #{userId}
      AND r.deleted = 0
      AND r.status = 1
    """)
    List<String> selectDataScopesByUserId(Long userId);
}