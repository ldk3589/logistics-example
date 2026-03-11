package com.dk.logistics.module.system.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.common.exception.BusinessException;
import com.dk.logistics.common.utils.SecurityUtils;
import com.dk.logistics.module.system.role.entity.SysRole;
import com.dk.logistics.module.system.role.mapper.SysRoleMapper;
import com.dk.logistics.module.system.user.dto.UserAddDTO;
import com.dk.logistics.module.system.user.dto.UserQueryDTO;
import com.dk.logistics.module.system.user.dto.UserUpdateDTO;
import com.dk.logistics.module.system.user.entity.SysUser;
import com.dk.logistics.module.system.user.entity.SysUserRole;
import com.dk.logistics.module.system.user.mapper.SysUserMapper;
import com.dk.logistics.module.system.user.mapper.SysUserRoleMapper;
import com.dk.logistics.module.system.user.service.UserService;
import com.dk.logistics.module.system.user.vo.UserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(SysUserMapper sysUserMapper,
                           SysUserRoleMapper sysUserRoleMapper,
                           SysRoleMapper sysRoleMapper,
                           PasswordEncoder passwordEncoder) {
        this.sysUserMapper = sysUserMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void addUser(UserAddDTO dto) {
        SysUser exist = sysUserMapper.selectByUsername(dto.getUsername());
        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setDeptId(dto.getDeptId());
        user.setStatus(dto.getStatus());
        user.setIsAdmin(0);
        user.setRemark(dto.getRemark());
        user.setDeleted(0);
        user.setCreatedBy(SecurityUtils.getUserId());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedBy(SecurityUtils.getUserId());
        user.setUpdatedAt(LocalDateTime.now());

        int rows = sysUserMapper.insert(user);
        if (rows <= 0) {
            throw new BusinessException("新增用户失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long userId, List<Long> roleIds) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getDeleted() != 0) {
            throw new BusinessException("用户不存在");
        }

        LambdaQueryWrapper<SysUserRole> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysUserRole::getUserId, userId);
        sysUserRoleMapper.delete(deleteWrapper);

        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        for (Long roleId : roleIds) {
            SysRole role = sysRoleMapper.selectById(roleId);
            if (role == null || role.getDeleted() != 0) {
                throw new BusinessException("存在无效角色ID: " + roleId);
            }

            SysUserRole item = new SysUserRole();
            item.setUserId(userId);
            item.setRoleId(roleId);
            item.setCreatedAt(LocalDateTime.now());
            sysUserRoleMapper.insert(item);
        }
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getDeleted() != 0) {
            throw new BusinessException("用户不存在");
        }
        return sysUserRoleMapper.selectRoleIdsByUserId(userId);
    }

    private UserVO toVO(SysUser user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setRealName(user.getRealName());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setDeptId(user.getDeptId());
        vo.setStatus(user.getStatus());
        vo.setIsAdmin(user.getIsAdmin());
        vo.setRemark(user.getRemark());
        return vo;
    }

    @Override
    public UserVO getUserById(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getDeleted() != 0) {
            throw new BusinessException("用户不存在");
        }
        return toVO(user);
    }

    @Override
    public void updateUser(Long userId, UserUpdateDTO dto) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getDeleted() != 0) {
            throw new BusinessException("用户不存在");
        }

        user.setNickname(dto.getNickname());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setDeptId(dto.getDeptId());
        user.setStatus(dto.getStatus());
        user.setRemark(dto.getRemark());
        user.setUpdatedBy(SecurityUtils.getUserId());
        user.setUpdatedAt(LocalDateTime.now());

        int rows = sysUserMapper.updateById(user);
        if (rows <= 0) {
            throw new BusinessException("修改用户失败");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getDeleted() != 0) {
            throw new BusinessException("用户不存在");
        }

        user.setDeleted(1);
        user.setUpdatedBy(SecurityUtils.getUserId());
        user.setUpdatedAt(LocalDateTime.now());

        int rows = sysUserMapper.updateById(user);
        if (rows <= 0) {
            throw new BusinessException("删除用户失败");
        }
    }

    @Override
    public void changeUserStatus(Long userId, Integer status) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null || user.getDeleted() != 0) {
            throw new BusinessException("用户不存在");
        }

        user.setStatus(status);
        user.setUpdatedBy(SecurityUtils.getUserId());
        user.setUpdatedAt(LocalDateTime.now());

        int rows = sysUserMapper.updateById(user);
        if (rows <= 0) {
            throw new BusinessException("修改用户状态失败");
        }
    }

    @Override
    public PageResult<UserVO> listUsers(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getUsername())) {
                wrapper.like(SysUser::getUsername, queryDTO.getUsername());
            }
            if (StringUtils.hasText(queryDTO.getNickname())) {
                wrapper.like(SysUser::getNickname, queryDTO.getNickname());
            }
            if (queryDTO.getStatus() != null) {
                wrapper.eq(SysUser::getStatus, queryDTO.getStatus());
            }
        }

        wrapper.orderByAsc(SysUser::getId);

        Page<SysUser> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<SysUser> resultPage = sysUserMapper.selectPage(page, wrapper);

        List<UserVO> records = resultPage.getRecords()
                .stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return new PageResult<>(resultPage.getTotal(), records);
    }
}