package com.dk.logistics.module.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.system.user.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("""
        SELECT role_id
        FROM sys_user_role
        WHERE user_id = #{userId}
        """)
    List<Long> selectRoleIdsByUserId(Long userId);
}