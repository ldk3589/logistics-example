package com.dk.logistics.module.system.permission.dto;
import com.dk.logistics.common.api.PageQuery;

public class PermissionQueryDTO extends PageQuery {
    private String permissionName;
    private String permissionCode;
    private Integer status;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}