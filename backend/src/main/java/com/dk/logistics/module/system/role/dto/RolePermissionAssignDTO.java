package com.dk.logistics.module.system.role.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RolePermissionAssignDTO {

    @NotNull(message = "权限ID列表不能为空")
    private List<Long> permissionIds;

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}