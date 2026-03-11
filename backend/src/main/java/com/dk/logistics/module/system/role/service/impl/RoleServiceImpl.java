package com.dk.logistics.module.system.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dk.logistics.common.exception.BusinessException;
import com.dk.logistics.common.utils.SecurityUtils;
import com.dk.logistics.module.system.role.dto.RoleAddDTO;
import com.dk.logistics.module.system.role.dto.RoleQueryDTO;
import com.dk.logistics.module.system.role.dto.RoleUpdateDTO;
import com.dk.logistics.module.system.role.entity.SysRole;
import com.dk.logistics.module.system.role.entity.SysRoleMenu;
import com.dk.logistics.module.system.role.entity.SysRolePermission;
import com.dk.logistics.module.system.role.mapper.SysRoleMapper;
import com.dk.logistics.module.system.role.mapper.SysRoleMenuMapper;
import com.dk.logistics.module.system.role.mapper.SysRolePermissionMapper;
import com.dk.logistics.module.system.role.service.RoleService;
import com.dk.logistics.module.system.role.vo.RoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    public RoleServiceImpl(SysRoleMapper sysRoleMapper,
                           SysRolePermissionMapper sysRolePermissionMapper,
                           SysRoleMenuMapper sysRoleMenuMapper) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysRolePermissionMapper = sysRolePermissionMapper;
        this.sysRoleMenuMapper = sysRoleMenuMapper;
    }

    @Override
    public List<RoleVO> listRoles(RoleQueryDTO queryDTO) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getRoleName())) {
                wrapper.like(SysRole::getRoleName, queryDTO.getRoleName());
            }
            if (StringUtils.hasText(queryDTO.getRoleCode())) {
                wrapper.like(SysRole::getRoleCode, queryDTO.getRoleCode());
            }
            if (queryDTO.getStatus() != null) {
                wrapper.eq(SysRole::getStatus, queryDTO.getStatus());
            }
        }

        wrapper.orderByAsc(SysRole::getSortNum).orderByAsc(SysRole::getId);

        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public void addRole(RoleAddDTO dto) {
        SysRole exist = sysRoleMapper.selectByRoleCode(dto.getRoleCode());
        if (exist != null) {
            throw new BusinessException("角色编码已存在");
        }

        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setDataScope(dto.getDataScope());
        role.setStatus(dto.getStatus());
        role.setSortNum(dto.getSortNum() == null ? 0 : dto.getSortNum());
        role.setRemark(dto.getRemark());
        role.setDeleted(0);
        role.setCreatedBy(SecurityUtils.getUserId());
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedBy(SecurityUtils.getUserId());
        role.setUpdatedAt(LocalDateTime.now());

        int rows = sysRoleMapper.insert(role);
        if (rows <= 0) {
            throw new BusinessException("新增角色失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null || role.getDeleted() != 0) {
            throw new BusinessException("角色不存在");
        }

        LambdaQueryWrapper<SysRolePermission> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysRolePermission::getRoleId, roleId);
        sysRolePermissionMapper.delete(deleteWrapper);

        if (permissionIds == null || permissionIds.isEmpty()) {
            return;
        }

        for (Long permissionId : permissionIds) {
            SysRolePermission item = new SysRolePermission();
            item.setRoleId(roleId);
            item.setPermissionId(permissionId);
            item.setCreatedAt(LocalDateTime.now());
            sysRolePermissionMapper.insert(item);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long roleId, List<Long> menuIds) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null || role.getDeleted() != 0) {
            throw new BusinessException("角色不存在");
        }

        LambdaQueryWrapper<SysRoleMenu> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysRoleMenu::getRoleId, roleId);
        sysRoleMenuMapper.delete(deleteWrapper);

        if (menuIds == null || menuIds.isEmpty()) {
            return;
        }

        for (Long menuId : menuIds) {
            SysRoleMenu item = new SysRoleMenu();
            item.setRoleId(roleId);
            item.setMenuId(menuId);
            item.setCreatedAt(LocalDateTime.now());
            sysRoleMenuMapper.insert(item);
        }
    }

    private RoleVO toVO(SysRole role) {
        RoleVO vo = new RoleVO();
        vo.setId(role.getId());
        vo.setRoleName(role.getRoleName());
        vo.setRoleCode(role.getRoleCode());
        vo.setDataScope(role.getDataScope());
        vo.setStatus(role.getStatus());
        vo.setSortNum(role.getSortNum());
        vo.setRemark(role.getRemark());
        return vo;
    }

    @Override
    public RoleVO getRoleById(Long roleId) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null || role.getDeleted() != 0) {
            throw new BusinessException("角色不存在");
        }
        return toVO(role);
    }

    @Override
    public void updateRole(Long roleId, RoleUpdateDTO dto) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null || role.getDeleted() != 0) {
            throw new BusinessException("角色不存在");
        }

        role.setRoleName(dto.getRoleName());
        role.setDataScope(dto.getDataScope());
        role.setStatus(dto.getStatus());
        role.setSortNum(dto.getSortNum() == null ? 0 : dto.getSortNum());
        role.setRemark(dto.getRemark());
        role.setUpdatedBy(SecurityUtils.getUserId());
        role.setUpdatedAt(LocalDateTime.now());

        int rows = sysRoleMapper.updateById(role);
        if (rows <= 0) {
            throw new BusinessException("修改角色失败");
        }
    }

    @Override
    public void deleteRole(Long roleId) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null || role.getDeleted() != 0) {
            throw new BusinessException("角色不存在");
        }

        role.setDeleted(1);
        role.setUpdatedBy(SecurityUtils.getUserId());
        role.setUpdatedAt(LocalDateTime.now());

        int rows = sysRoleMapper.updateById(role);
        if (rows <= 0) {
            throw new BusinessException("删除角色失败");
        }
    }

    @Override
    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null || role.getDeleted() != 0) {
            throw new BusinessException("角色不存在");
        }
        return sysRolePermissionMapper.selectPermissionIdsByRoleId(roleId);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null || role.getDeleted() != 0) {
            throw new BusinessException("角色不存在");
        }
        return sysRoleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public void changeDataScope(Long roleId, String dataScope) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null || role.getDeleted() != 0) {
            throw new BusinessException("角色不存在");
        }

        role.setDataScope(dataScope);
        role.setUpdatedBy(SecurityUtils.getUserId());
        role.setUpdatedAt(LocalDateTime.now());

        int rows = sysRoleMapper.updateById(role);
        if (rows <= 0) {
            throw new BusinessException("修改数据范围失败");
        }
    }
}