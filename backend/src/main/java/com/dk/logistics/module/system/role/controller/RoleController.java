package com.dk.logistics.module.system.role.controller;

import com.dk.logistics.common.api.ApiResult;
import com.dk.logistics.framework.annotation.OperationLog;
import com.dk.logistics.module.system.role.dto.*;
import com.dk.logistics.module.system.role.service.RoleService;
import com.dk.logistics.module.system.role.vo.RoleVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:role:list')")
    public ApiResult<List<RoleVO>> listRoles(RoleQueryDTO queryDTO) {
        return ApiResult.success(roleService.listRoles(queryDTO));
    }

    @OperationLog(module = "角色管理", name = "新增角色")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add')")
    public ApiResult<Void> addRole(@Valid @RequestBody RoleAddDTO dto) {
        roleService.addRole(dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "角色管理", name = "分配角色权限")
    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public ApiResult<Void> assignPermissions(@PathVariable("id") Long roleId,
                                             @Valid @RequestBody RolePermissionAssignDTO dto) {
        roleService.assignPermissions(roleId, dto.getPermissionIds());
        return ApiResult.success(null);
    }

    @OperationLog(module = "角色管理", name = "分配角色菜单")
    @PostMapping("/{id}/menus")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public ApiResult<Void> assignMenus(@PathVariable("id") Long roleId,
                                       @Valid @RequestBody RoleMenuAssignDTO dto) {
        roleService.assignMenus(roleId, dto.getMenuIds());
        return ApiResult.success(null);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public ApiResult<RoleVO> getRoleById(@PathVariable("id") Long roleId) {
        return ApiResult.success(roleService.getRoleById(roleId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public ApiResult<Void> updateRole(@PathVariable("id") Long roleId,
                                      @Valid @RequestBody RoleUpdateDTO dto) {
        roleService.updateRole(roleId, dto);
        return ApiResult.success(null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public ApiResult<Void> deleteRole(@PathVariable("id") Long roleId) {
        roleService.deleteRole(roleId);
        return ApiResult.success(null);
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public ApiResult<List<Long>> getPermissionIds(@PathVariable("id") Long roleId) {
        return ApiResult.success(roleService.getPermissionIdsByRoleId(roleId));
    }

    @GetMapping("/{id}/menus")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public ApiResult<List<Long>> getMenuIds(@PathVariable("id") Long roleId) {
        return ApiResult.success(roleService.getMenuIdsByRoleId(roleId));
    }
}