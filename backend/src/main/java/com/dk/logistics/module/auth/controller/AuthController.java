package com.dk.logistics.module.auth.controller;

import com.dk.logistics.common.api.ApiResult;
import com.dk.logistics.module.auth.dto.LoginDTO;
import com.dk.logistics.module.auth.dto.RegisterDTO;
import com.dk.logistics.module.auth.service.AuthService;
import com.dk.logistics.module.auth.vo.LoginVO;
import com.dk.logistics.module.system.menu.vo.MenuTreeVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResult<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return ApiResult.success(authService.login(dto));
    }

    @PostMapping("/register")
    public ApiResult<Void> register(@Valid @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return ApiResult.success(null);
    }

    @GetMapping("/menus")
    public ApiResult<List<MenuTreeVO>> getCurrentUserMenus() {
        return ApiResult.success(authService.getCurrentUserMenus());
    }
}