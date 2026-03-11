package com.dk.logistics.module.system.role.service;

import com.dk.logistics.module.system.role.dto.*;
import com.dk.logistics.module.system.role.vo.RoleVO;

import java.util.List;

public interface RoleService {
    List<RoleVO> listRoles(RoleQueryDTO queryDTO);

    void addRole(RoleAddDTO dto);

    void assignPermissions(Long roleId, List<Long> permissionIds);

    void assignMenus(Long roleId, List<Long> menuIds);

    RoleVO getRoleById(Long roleId);

    void updateRole(Long roleId, RoleUpdateDTO dto);

    void deleteRole(Long roleId);

    List<Long> getPermissionIdsByRoleId(Long roleId);

    List<Long> getMenuIdsByRoleId(Long roleId);

    void changeDataScope(Long roleId, String dataScope);
}