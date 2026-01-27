package com.dk.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dk.logistics.config.AdminPasswordConfig;
import com.dk.logistics.dto.LoginDTO;
import com.dk.logistics.dto.RegisterDTO;
import com.dk.logistics.entity.SysUser;
import com.dk.logistics.mapper.UserMapper;
import com.dk.logistics.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminPasswordConfig adminPasswordConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO dto) {
        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        String username = dto.getUsername().trim();
        String password = dto.getPassword().trim();

        if (username.contains(" ") || password.contains(" ")) {
            throw new RuntimeException("用户名或密码中间不能包含空格");
        }

        // 用户名是否已存在
        if (userMapper.selectByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        //决定角色（核心）
        String role = "USER";
        String adminPwd = dto.getAdminPassword();

        if (adminPwd != null && !adminPwd.isBlank()) {
            if (adminPwd.equals(adminPasswordConfig.getAdminL1())) {
                role = "ADMIN_L1";
            } else if (adminPwd.equals(adminPasswordConfig.getAdminL2())) {
                role = "ADMIN_L2";
            } else {
                throw new RuntimeException("管理员密码错误");
            }
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        userMapper.insert(user);
        return "注册成功";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO dto) {

        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
        );

        if (user == null ||
                !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        return jwtUtil.generateToken(user);
    }
}
