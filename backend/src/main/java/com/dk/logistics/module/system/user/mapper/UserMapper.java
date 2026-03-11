package com.dk.logistics.module.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.dk.logistics.module.system.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
    @Select("select * from sys_user where username = #{username} limit 1")
    SysUser selectByUsername(String username);
}
