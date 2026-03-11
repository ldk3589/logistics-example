package com.dk.logistics.module.system.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.common.exception.BusinessException;
import com.dk.logistics.common.utils.SecurityUtils;
import com.dk.logistics.module.system.permission.dto.PermissionAddDTO;
import com.dk.logistics.module.system.permission.dto.PermissionQueryDTO;
import com.dk.logistics.module.system.permission.dto.PermissionUpdateDTO;
import com.dk.logistics.module.system.permission.entity.SysPermission;
import com.dk.logistics.module.system.permission.mapper.SysPermissionMapper;
import com.dk.logistics.module.system.permission.service.PermissionService;
import com.dk.logistics.module.system.permission.vo.PermissionVO;
import com.dk.logistics.module.system.role.mapper.SysRolePermissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final SysPermissionMapper sysPermissionMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;

    public PermissionServiceImpl(SysPermissionMapper sysPermissionMapper,
                                 SysRolePermissionMapper sysRolePermissionMapper) {
        this.sysPermissionMapper = sysPermissionMapper;
        this.sysRolePermissionMapper = sysRolePermissionMapper;
    }

    @Override
    public PageResult<PermissionVO> listPermissions(PermissionQueryDTO queryDTO) {
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPermission::getDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getPermissionName())) {
                wrapper.like(SysPermission::getPermissionName, queryDTO.getPermissionName());
            }
            if (StringUtils.hasText(queryDTO.getPermissionCode())) {
                wrapper.like(SysPermission::getPermissionCode, queryDTO.getPermissionCode());
            }
            if (queryDTO.getStatus() != null) {
                wrapper.eq(SysPermission::getStatus, queryDTO.getStatus());
            }
        }

        wrapper.orderByAsc(SysPermission::getId);

        Page<SysPermission> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<SysPermission> resultPage = sysPermissionMapper.selectPage(page, wrapper);

        List<PermissionVO> records = resultPage.getRecords()
                .stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return new PageResult<>(resultPage.getTotal(), records);
    }

    @Override
    public void addPermission(PermissionAddDTO dto) {
        SysPermission exist = sysPermissionMapper.selectByPermissionCode(dto.getPermissionCode());
        if (exist != null) {
            throw new BusinessException("权限编码已存在");
        }

        SysPermission permission = new SysPermission();
        permission.setPermissionName(dto.getPermissionName());
        permission.setPermissionCode(dto.getPermissionCode());
        permission.setApiUri(dto.getApiUri());
        permission.setHttpMethod(dto.getHttpMethod());
        permission.setPermissionType(dto.getPermissionType());
        permission.setStatus(dto.getStatus());
        permission.setRemark(dto.getRemark());
        permission.setDeleted(0);
        permission.setCreatedBy(SecurityUtils.getUserId());
        permission.setCreatedAt(LocalDateTime.now());
        permission.setUpdatedBy(SecurityUtils.getUserId());
        permission.setUpdatedAt(LocalDateTime.now());

        int rows = sysPermissionMapper.insert(permission);
        if (rows <= 0) {
            throw new BusinessException("新增权限失败");
        }
    }

    @Override
    public PermissionVO getPermissionById(Long id) {
        SysPermission permission = sysPermissionMapper.selectById(id);
        if (permission == null || permission.getDeleted() != 0) {
            throw new BusinessException("权限不存在");
        }
        return toVO(permission);
    }

    @Override
    public void updatePermission(Long id, PermissionUpdateDTO dto) {
        SysPermission permission = sysPermissionMapper.selectById(id);
        if (permission == null || permission.getDeleted() != 0) {
            throw new BusinessException("权限不存在");
        }

        SysPermission exist = sysPermissionMapper.selectByPermissionCode(dto.getPermissionCode());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException("权限编码已存在");
        }

        permission.setPermissionName(dto.getPermissionName());
        permission.setPermissionCode(dto.getPermissionCode());
        permission.setApiUri(dto.getApiUri());
        permission.setHttpMethod(dto.getHttpMethod());
        permission.setPermissionType(dto.getPermissionType());
        permission.setStatus(dto.getStatus());
        permission.setRemark(dto.getRemark());
        permission.setUpdatedBy(SecurityUtils.getUserId());
        permission.setUpdatedAt(LocalDateTime.now());

        int rows = sysPermissionMapper.updateById(permission);
        if (rows <= 0) {
            throw new BusinessException("修改权限失败");
        }
    }

    @Override
    public void deletePermission(Long id) {
        SysPermission permission = sysPermissionMapper.selectById(id);
        if (permission == null || permission.getDeleted() != 0) {
            throw new BusinessException("权限不存在");
        }

        Long bindCount = sysRolePermissionMapper.countByPermissionId(id);
        if (bindCount != null && bindCount > 0) {
            throw new BusinessException("该权限已被角色绑定，不能删除");
        }

        permission.setDeleted(1);
        permission.setUpdatedBy(SecurityUtils.getUserId());
        permission.setUpdatedAt(LocalDateTime.now());

        int rows = sysPermissionMapper.updateById(permission);
        if (rows <= 0) {
            throw new BusinessException("删除权限失败");
        }
    }

    private PermissionVO toVO(SysPermission permission) {
        PermissionVO vo = new PermissionVO();
        vo.setId(permission.getId());
        vo.setPermissionName(permission.getPermissionName());
        vo.setPermissionCode(permission.getPermissionCode());
        vo.setApiUri(permission.getApiUri());
        vo.setHttpMethod(permission.getHttpMethod());
        vo.setPermissionType(permission.getPermissionType());
        vo.setStatus(permission.getStatus());
        vo.setRemark(permission.getRemark());
        return vo;
    }
}