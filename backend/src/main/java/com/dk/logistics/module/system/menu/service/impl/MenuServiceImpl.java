package com.dk.logistics.module.system.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dk.logistics.common.exception.BusinessException;
import com.dk.logistics.common.utils.SecurityUtils;
import com.dk.logistics.common.utils.TreeUtils;
import com.dk.logistics.module.system.menu.dto.MenuAddDTO;
import com.dk.logistics.module.system.menu.dto.MenuQueryDTO;
import com.dk.logistics.module.system.menu.entity.SysMenu;
import com.dk.logistics.module.system.menu.mapper.SysMenuMapper;
import com.dk.logistics.module.system.menu.service.MenuService;
import com.dk.logistics.module.system.menu.vo.MenuTreeVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final SysMenuMapper sysMenuMapper;

    public MenuServiceImpl(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public List<MenuTreeVO> getCurrentUserMenuTree(Long userId) {
        List<SysMenu> menus = sysMenuMapper.selectMenusByUserId(userId);
        List<MenuTreeVO> nodes = menus.stream().map(this::toVO).collect(Collectors.toList());
        return TreeUtils.buildMenuTree(nodes);
    }

    @Override
    public List<MenuTreeVO> listMenus(MenuQueryDTO queryDTO) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getMenuName())) {
                wrapper.like(SysMenu::getMenuName, queryDTO.getMenuName());
            }
            if (StringUtils.hasText(queryDTO.getMenuType())) {
                wrapper.eq(SysMenu::getMenuType, queryDTO.getMenuType());
            }
            if (queryDTO.getStatus() != null) {
                wrapper.eq(SysMenu::getStatus, queryDTO.getStatus());
            }
        }

        wrapper.orderByAsc(SysMenu::getSortNum).orderByAsc(SysMenu::getId);

        List<SysMenu> list = sysMenuMapper.selectList(wrapper);
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public List<MenuTreeVO> listMenuTree() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getDeleted, 0);
        wrapper.orderByAsc(SysMenu::getSortNum).orderByAsc(SysMenu::getId);

        List<SysMenu> list = sysMenuMapper.selectList(wrapper);
        List<MenuTreeVO> nodes = list.stream().map(this::toVO).collect(Collectors.toList());
        return TreeUtils.buildMenuTree(nodes);
    }

    @Override
    public void addMenu(MenuAddDTO dto) {
        SysMenu exist = sysMenuMapper.selectByParentIdAndMenuName(dto.getParentId(), dto.getMenuName());
        if (exist != null) {
            throw new BusinessException("同级菜单名称已存在");
        }

        SysMenu menu = new SysMenu();
        menu.setParentId(dto.getParentId());
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setPath(dto.getPath());
        menu.setComponent(dto.getComponent());
        menu.setRouteName(dto.getRouteName());
        menu.setIcon(dto.getIcon());
        menu.setPermissionCode(dto.getPermissionCode());
        menu.setVisible(dto.getVisible());
        menu.setKeepAlive(dto.getKeepAlive());
        menu.setSortNum(dto.getSortNum() == null ? 0 : dto.getSortNum());
        menu.setStatus(dto.getStatus());
        menu.setRemark(dto.getRemark());
        menu.setDeleted(0);

        int rows = sysMenuMapper.insert(menu);
        if (rows <= 0) {
            throw new BusinessException("新增菜单失败");
        }
    }

    private MenuTreeVO toVO(SysMenu menu) {
        MenuTreeVO vo = new MenuTreeVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setMenuName(menu.getMenuName());
        vo.setMenuType(menu.getMenuType());
        vo.setPath(menu.getPath());
        vo.setComponent(menu.getComponent());
        vo.setRouteName(menu.getRouteName());
        vo.setIcon(menu.getIcon());
        vo.setPermissionCode(menu.getPermissionCode());
        vo.setVisible(menu.getVisible());
        vo.setKeepAlive(menu.getKeepAlive());
        vo.setSortNum(menu.getSortNum());
        return vo;
    }

    @Override
    public MenuTreeVO getMenuById(Long id) {
        SysMenu menu = sysMenuMapper.selectById(id);
        if (menu == null || menu.getDeleted() != 0) {
            throw new BusinessException("菜单不存在");
        }
        return toVO(menu);
    }

    @Override
    public void updateMenu(Long id, MenuAddDTO dto) {
        SysMenu menu = sysMenuMapper.selectById(id);
        if (menu == null || menu.getDeleted() != 0) {
            throw new BusinessException("菜单不存在");
        }

        SysMenu exist = sysMenuMapper.selectByParentIdAndMenuName(dto.getParentId(), dto.getMenuName());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException("同级菜单名称已存在");
        }

        menu.setParentId(dto.getParentId());
        menu.setMenuName(dto.getMenuName());
        menu.setMenuType(dto.getMenuType());
        menu.setPath(dto.getPath());
        menu.setComponent(dto.getComponent());
        menu.setRouteName(dto.getRouteName());
        menu.setIcon(dto.getIcon());
        menu.setPermissionCode(dto.getPermissionCode());
        menu.setVisible(dto.getVisible());
        menu.setKeepAlive(dto.getKeepAlive());
        menu.setSortNum(dto.getSortNum() == null ? 0 : dto.getSortNum());
        menu.setStatus(dto.getStatus());
        menu.setRemark(dto.getRemark());

        int rows = sysMenuMapper.updateById(menu);
        if (rows <= 0) {
            throw new BusinessException("修改菜单失败");
        }
    }

    @Override
    public void deleteMenu(Long id) {
        SysMenu menu = sysMenuMapper.selectById(id);
        if (menu == null || menu.getDeleted() != 0) {
            throw new BusinessException("菜单不存在");
        }

        Long childCount = sysMenuMapper.countChildrenByMenuId(id);
        if (childCount != null && childCount > 0) {
            throw new BusinessException("存在子菜单，不能删除");
        }

        Long bindCount = sysMenuMapper.countRoleMenuBindByMenuId(id);
        if (bindCount != null && bindCount > 0) {
            throw new BusinessException("该菜单已被角色绑定，不能删除");
        }

        menu.setDeleted(1);

        int rows = sysMenuMapper.updateById(menu);
        if (rows <= 0) {
            throw new BusinessException("删除菜单失败");
        }
    }
}