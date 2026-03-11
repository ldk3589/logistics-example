package com.dk.logistics.module.auth.service;

import com.dk.logistics.module.auth.dto.LoginDTO;
import com.dk.logistics.module.auth.dto.RegisterDTO;
import com.dk.logistics.module.auth.vo.LoginVO;
import com.dk.logistics.module.system.menu.vo.MenuTreeVO;

import java.util.List;

public interface AuthService {
    LoginVO login(LoginDTO dto);

    List<MenuTreeVO> getCurrentUserMenus();

    void register(RegisterDTO dto);
}