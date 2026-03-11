package com.dk.logistics.module.system.user.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.system.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("""
        SELECT id, username, password, nickname, real_name, phone, email, avatar,
               dept_id, status, is_admin, last_login_ip, last_login_time, remark,
               created_by, created_at, updated_by, updated_at, deleted
        FROM sys_user
        WHERE username = #{username} AND deleted = 0
        LIMIT 1
        """)
    SysUser selectByUsername(String username);
}