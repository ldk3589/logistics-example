package com.dk.logistics.module.system.menu.controller;

import com.dk.logistics.common.api.ApiResult;
import com.dk.logistics.framework.annotation.OperationLog;
import com.dk.logistics.module.system.menu.dto.MenuAddDTO;
import com.dk.logistics.module.system.menu.dto.MenuQueryDTO;
import com.dk.logistics.module.system.menu.service.MenuService;
import com.dk.logistics.module.system.menu.vo.MenuTreeVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public ApiResult<List<MenuTreeVO>> listMenus(MenuQueryDTO queryDTO) {
        return ApiResult.success(menuService.listMenus(queryDTO));
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public ApiResult<List<MenuTreeVO>> listMenuTree() {
        return ApiResult.success(menuService.listMenuTree());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public ApiResult<MenuTreeVO> getMenuById(@PathVariable("id") Long id) {
        return ApiResult.success(menuService.getMenuById(id));
    }

    @OperationLog(module = "菜单管理", name = "新增菜单")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:menu:add')")
    public ApiResult<Void> addMenu(@Valid @RequestBody MenuAddDTO dto) {
        menuService.addMenu(dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "菜单管理", name = "修改菜单")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public ApiResult<Void> updateMenu(@PathVariable("id") Long id,
                                      @Valid @RequestBody MenuAddDTO dto) {
        menuService.updateMenu(id, dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "菜单管理", name = "删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public ApiResult<Void> deleteMenu(@PathVariable("id") Long id) {
        menuService.deleteMenu(id);
        return ApiResult.success(null);
    }
}