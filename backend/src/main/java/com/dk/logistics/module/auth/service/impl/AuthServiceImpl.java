package com.dk.logistics.module.auth.service.impl;

import com.dk.logistics.common.exception.BusinessException;
import com.dk.logistics.common.utils.IpUtils;
import com.dk.logistics.common.utils.JwtUtils;
import com.dk.logistics.common.utils.SecurityUtils;
import com.dk.logistics.common.utils.UserAgentUtils;
import com.dk.logistics.module.auth.dto.LoginDTO;
import com.dk.logistics.module.auth.dto.RegisterDTO;
import com.dk.logistics.module.auth.service.AuthService;
import com.dk.logistics.module.auth.vo.LoginVO;
import com.dk.logistics.module.system.log.entity.SysLoginLog;
import com.dk.logistics.module.system.log.service.LogService;
import com.dk.logistics.module.system.menu.service.MenuService;
import com.dk.logistics.module.system.menu.vo.MenuTreeVO;
import com.dk.logistics.module.system.role.entity.SysRole;
import com.dk.logistics.module.system.role.mapper.SysRoleMapper;
import com.dk.logistics.module.system.user.entity.SysUser;
import com.dk.logistics.module.system.user.entity.SysUserRole;
import com.dk.logistics.module.system.user.mapper.SysUserMapper;
import com.dk.logistics.module.system.user.mapper.SysUserRoleMapper;
import com.dk.logistics.security.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final MenuService menuService;
    private final LogService logService;
    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final SysRoleMapper sysRoleMapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           MenuService menuService,
                           LogService logService,
                           SysUserMapper sysUserMapper,
                           SysUserRoleMapper sysUserRoleMapper,
                           PasswordEncoder passwordEncoder,
                           SysRoleMapper sysRoleMapper) {
        this.authenticationManager = authenticationManager;
        this.menuService = menuService;
        this.logService = logService;
        this.sysUserMapper=sysUserMapper;
        this.sysUserRoleMapper=sysUserRoleMapper;
        this.passwordEncoder=passwordEncoder;
        this.sysRoleMapper=sysRoleMapper;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (Exception e) {
            saveLoginLog(dto.getUsername(), 0, "用户名或密码错误");
            throw new BusinessException("用户名或密码错误");
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = JwtUtils.createToken(loginUser.getUserId(), loginUser.getUsername());

        saveLoginLog(loginUser.getUsername(), 1, "登录成功");

        LoginVO vo = new LoginVO();
        vo.setToken(token);

        LoginVO.UserInfoVO userInfo = new LoginVO.UserInfoVO();
        userInfo.setId(loginUser.getUserId());
        userInfo.setUsername(loginUser.getUsername());
        userInfo.setNickname(loginUser.getUsername());
        vo.setUserInfo(userInfo);

        vo.setRoles(loginUser.getRoles());
        vo.setPermissions(loginUser.getPermissions());
        vo.setMenus(menuService.getCurrentUserMenuTree(loginUser.getUserId()));

        return vo;
    }

    @Override
    public List<MenuTreeVO> getCurrentUserMenus() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new BusinessException("未获取到当前登录用户");
        }
        return menuService.getCurrentUserMenuTree(userId);
    }

    private void saveLoginLog(String username, Integer status, String message) {
        SysLoginLog log = new SysLoginLog();
        log.setUsername(username);
        log.setLoginStatus(status);
        log.setMessage(message);
        log.setLoginLocation("unknown");
        log.setCreatedAt(LocalDateTime.now());

        HttpServletRequest request = getRequest();
        if (request != null) {
            String userAgent = request.getHeader("User-Agent");
            log.setLoginIp(IpUtils.getIpAddress(request));
            log.setBrowser(UserAgentUtils.getBrowser(userAgent));
            log.setOs(UserAgentUtils.getOs(userAgent));
        } else {
            log.setLoginIp("unknown");
            log.setBrowser("Unknown");
            log.setOs("Unknown");
        }

        logService.saveLoginLog(log);
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    @Override
    public void register(RegisterDTO dto) {
        SysUser exist = sysUserMapper.selectByUsername(dto.getUsername());
        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setDeptId(dto.getDeptId());
        user.setStatus(1);
        user.setIsAdmin(0);
        user.setDeleted(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        int rows = sysUserMapper.insert(user);
        if (rows <= 0) {
            throw new BusinessException("注册失败");
        }

        // 默认给普通用户角色：ORDER_USER
        SysRole role = sysRoleMapper.selectByRoleCode("ORDER_USER");
        if (role != null && role.getDeleted() == 0) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRole.setCreatedAt(LocalDateTime.now());
            sysUserRoleMapper.insert(userRole);
        }
    }
}