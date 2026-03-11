package com.dk.logistics.module.system.user.controller;

import com.dk.logistics.common.api.ApiResult;
import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.framework.annotation.OperationLog;
import com.dk.logistics.module.system.user.dto.*;
import com.dk.logistics.module.system.user.service.UserService;
import com.dk.logistics.module.system.user.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @OperationLog(module="用户管理",name = "新增用户")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    public ApiResult<Void> addUser(@Valid @RequestBody UserAddDTO dto) {
        userService.addUser(dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "用户管理", name = "分配用户角色")
    @PostMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public ApiResult<Void> assignRoles(@PathVariable("id") Long userId,
                                       @Valid @RequestBody UserRoleAssignDTO dto) {
        userService.assignRoles(userId, dto.getRoleIds());
        return ApiResult.success(null);
    }

    @GetMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public ApiResult<List<Long>> getRoleIdsByUserId(@PathVariable("id") Long userId) {
        return ApiResult.success(userService.getRoleIdsByUserId(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public ApiResult<UserVO> getUserById(@PathVariable("id") Long userId) {
        return ApiResult.success(userService.getUserById(userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public ApiResult<Void> updateUser(@PathVariable("id") Long userId,
                                      @Valid @RequestBody UserUpdateDTO dto) {
        userService.updateUser(userId, dto);
        return ApiResult.success(null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public ApiResult<Void> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ApiResult.success(null);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public ApiResult<Void> changeUserStatus(@PathVariable("id") Long userId,
                                            @Valid @RequestBody UserStatusDTO dto) {
        userService.changeUserStatus(userId, dto.getStatus());
        return ApiResult.success(null);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:user:list')")
    public ApiResult<PageResult<UserVO>> listUsers(UserQueryDTO queryDTO) {
        return ApiResult.success(userService.listUsers(queryDTO));
    }
}