package com.dk.logistics.module.system.menu.mapper;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.system.menu.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("""
        SELECT DISTINCT
            m.id,
            m.parent_id,
            m.menu_name,
            m.menu_type,
            m.path,
            m.component,
            m.route_name,
            m.icon,
            m.permission_code,
            m.visible,
            m.keep_alive,
            m.sort_num,
            m.status,
            m.remark,
            m.deleted
        FROM sys_menu m
        INNER JOIN sys_role_menu rm ON rm.menu_id = m.id
        INNER JOIN sys_user_role ur ON ur.role_id = rm.role_id
        WHERE ur.user_id = #{userId}
          AND m.deleted = 0
          AND m.status = 1
          AND m.visible = 1
        ORDER BY m.sort_num ASC, m.id ASC
        """)
    List<SysMenu> selectMenusByUserId(Long userId);

    @Select("""
    SELECT id, parent_id, menu_name, menu_type, path, component, route_name,
           icon, permission_code, visible, keep_alive, sort_num, status, remark, deleted
    FROM sys_menu
    WHERE parent_id = #{parentId}
      AND menu_name = #{menuName}
      AND deleted = 0
    LIMIT 1
    """)
    SysMenu selectByParentIdAndMenuName(Long parentId, String menuName);

    @Select("""
    SELECT COUNT(1)
    FROM sys_menu
    WHERE parent_id = #{menuId}
      AND deleted = 0
    """)
    Long countChildrenByMenuId(Long menuId);

    @Select("""
    SELECT COUNT(1)
    FROM sys_role_menu
    WHERE menu_id = #{menuId}
    """)
    Long countRoleMenuBindByMenuId(Long menuId);
}