package com.dk.logistics.module.system.permission.controller;

import com.dk.logistics.common.api.ApiResult;
import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.framework.annotation.OperationLog;
import com.dk.logistics.module.system.permission.dto.PermissionAddDTO;
import com.dk.logistics.module.system.permission.dto.PermissionQueryDTO;
import com.dk.logistics.module.system.permission.dto.PermissionUpdateDTO;
import com.dk.logistics.module.system.permission.service.PermissionService;
import com.dk.logistics.module.system.permission.vo.PermissionVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:permission:list')")
    public ApiResult<PageResult<PermissionVO>> listPermissions(PermissionQueryDTO queryDTO) {
        return ApiResult.success(permissionService.listPermissions(queryDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:permission:list')")
    public ApiResult<PermissionVO> getPermissionById(@PathVariable("id") Long id) {
        return ApiResult.success(permissionService.getPermissionById(id));
    }

    @OperationLog(module = "权限管理", name = "新增权限")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:permission:add')")
    public ApiResult<Void> addPermission(@Valid @RequestBody PermissionAddDTO dto) {
        permissionService.addPermission(dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "权限管理", name = "修改权限")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:permission:update')")
    public ApiResult<Void> updatePermission(@PathVariable("id") Long id,
                                            @Valid @RequestBody PermissionUpdateDTO dto) {
        permissionService.updatePermission(id, dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "权限管理", name = "删除权限")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    public ApiResult<Void> deletePermission(@PathVariable("id") Long id) {
        permissionService.deletePermission(id);
        return ApiResult.success(null);
    }
}