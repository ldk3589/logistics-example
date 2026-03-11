package com.dk.logistics.module.auth.vo;

import com.dk.logistics.module.system.menu.vo.MenuTreeVO;

import java.util.List;

public class LoginVO {
    private String token;
    private UserInfoVO userInfo;
    private List<String> roles;
    private List<String> permissions;
    private List<MenuTreeVO> menus;

    public static class UserInfoVO {
        private Long id;
        private String username;
        private String nickname;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoVO userInfo) {
        this.userInfo = userInfo;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<MenuTreeVO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuTreeVO> menus) {
        this.menus = menus;
    }
}