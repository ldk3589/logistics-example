package com.dk.logistics.module.system.menu.service;

import com.dk.logistics.module.system.menu.dto.MenuAddDTO;
import com.dk.logistics.module.system.menu.dto.MenuQueryDTO;
import com.dk.logistics.module.system.menu.vo.MenuTreeVO;

import java.util.List;

public interface MenuService {
    List<MenuTreeVO> getCurrentUserMenuTree(Long userId);

    List<MenuTreeVO> listMenus(MenuQueryDTO queryDTO);

    List<MenuTreeVO> listMenuTree();

    void addMenu(MenuAddDTO dto);

    MenuTreeVO getMenuById(Long id);

    void updateMenu(Long id, MenuAddDTO dto);

    void deleteMenu(Long id);
}