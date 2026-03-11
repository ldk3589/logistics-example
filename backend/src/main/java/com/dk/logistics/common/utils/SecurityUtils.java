package com.dk.logistics.common.utils;


import com.dk.logistics.entity.SysUser;
import com.dk.logistics.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static SysUser getCurrentUser() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            return null;
        }

        return ((LoginUser) auth.getPrincipal()).getUser();
    }
}
