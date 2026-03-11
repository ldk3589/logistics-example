package com.dk.logistics.module.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.system.role.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    @Select("""
    SELECT menu_id
    FROM sys_role_menu
    WHERE role_id = #{roleId}
    """)
    List<Long> selectMenuIdsByRoleId(Long roleId);
}