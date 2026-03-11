package com.dk.logistics.module.system.log.dto;

import com.dk.logistics.common.api.PageQuery;

public class LoginLogQueryDTO extends PageQuery {
    private String username;
    private Integer loginStatus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }
}