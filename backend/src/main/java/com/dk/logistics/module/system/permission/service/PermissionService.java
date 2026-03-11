package com.dk.logistics.module.system.permission.service;

import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.module.system.permission.dto.PermissionAddDTO;
import com.dk.logistics.module.system.permission.dto.PermissionQueryDTO;
import com.dk.logistics.module.system.permission.dto.PermissionUpdateDTO;
import com.dk.logistics.module.system.permission.vo.PermissionVO;

public interface PermissionService {
    PageResult<PermissionVO> listPermissions(PermissionQueryDTO queryDTO);

    void addPermission(PermissionAddDTO dto);

    PermissionVO getPermissionById(Long id);

    void updatePermission(Long id, PermissionUpdateDTO dto);

    void deletePermission(Long id);
}