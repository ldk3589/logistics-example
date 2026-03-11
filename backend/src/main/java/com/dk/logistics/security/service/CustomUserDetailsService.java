package com.dk.logistics.security.service;

import com.dk.logistics.module.system.permission.mapper.SysPermissionMapper;
import com.dk.logistics.module.system.role.mapper.SysRoleMapper;
import com.dk.logistics.module.system.user.entity.SysUser;
import com.dk.logistics.module.system.user.mapper.SysUserMapper;
import com.dk.logistics.security.model.LoginUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysPermissionMapper sysPermissionMapper;

    public CustomUserDetailsService(SysUserMapper sysUserMapper,
                                    SysRoleMapper sysRoleMapper,
                                    SysPermissionMapper sysPermissionMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysPermissionMapper = sysPermissionMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<String> roles = sysRoleMapper.selectRoleCodesByUserId(user.getId());
        List<String> permissions = sysPermissionMapper.selectPermissionCodesByUserId(user.getId());

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setPassword(user.getPassword());
        loginUser.setEnabled(user.getStatus() != null && user.getStatus() == 1);
        loginUser.setDeptId(user.getDeptId());
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        loginUser.setAuthorities(authorities);

        return loginUser;
    }
}