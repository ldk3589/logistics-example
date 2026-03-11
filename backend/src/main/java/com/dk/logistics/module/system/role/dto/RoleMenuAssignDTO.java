package com.dk.logistics.module.system.role.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RoleMenuAssignDTO {

    @NotNull(message = "菜单ID列表不能为空")
    private List<Long> menuIds;

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }
}