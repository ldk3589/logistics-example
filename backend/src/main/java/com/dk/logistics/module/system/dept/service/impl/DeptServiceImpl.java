package com.dk.logistics.module.system.dept.service.impl;

import com.dk.logistics.common.exception.BusinessException;
import com.dk.logistics.common.utils.SecurityUtils;
import com.dk.logistics.module.system.dept.dto.DeptAddDTO;
import com.dk.logistics.module.system.dept.dto.DeptUpdateDTO;
import com.dk.logistics.module.system.dept.entity.SysDept;
import com.dk.logistics.module.system.dept.mapper.SysDeptMapper;
import com.dk.logistics.module.system.dept.service.DeptService;
import com.dk.logistics.module.system.dept.vo.DeptTreeVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
        import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {

    private final SysDeptMapper sysDeptMapper;

    public DeptServiceImpl(SysDeptMapper sysDeptMapper) {
        this.sysDeptMapper = sysDeptMapper;
    }

    @Override
    public List<DeptTreeVO> listDeptTree() {
        List<SysDept> list = sysDeptMapper.selectAllActive();
        List<DeptTreeVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        return buildTree(voList, 0L);
    }

    @Override
    public DeptTreeVO getDeptById(Long id) {
        SysDept dept = sysDeptMapper.selectById(id);
        if (dept == null || dept.getDeleted() != 0) {
            throw new BusinessException("部门不存在");
        }
        return toVO(dept);
    }

    @Override
    public void addDept(DeptAddDTO dto) {
        if (dto.getParentId() != 0) {
            SysDept parent = sysDeptMapper.selectById(dto.getParentId());
            if (parent == null || parent.getDeleted() != 0) {
                throw new BusinessException("父部门不存在");
            }
        }

        SysDept exist = sysDeptMapper.selectByDeptCode(dto.getDeptCode());
        if (exist != null) {
            throw new BusinessException("部门编码已存在");
        }

        SysDept dept = new SysDept();
        dept.setParentId(dto.getParentId());
        dept.setDeptName(dto.getDeptName());
        dept.setDeptCode(dto.getDeptCode());
        dept.setLeaderUserId(dto.getLeaderUserId());
        dept.setSortNum(dto.getSortNum() == null ? 0 : dto.getSortNum());
        dept.setStatus(dto.getStatus());
        dept.setRemark(dto.getRemark());
        dept.setDeleted(0);
        dept.setCreatedBy(SecurityUtils.getUserId());
        dept.setCreatedAt(LocalDateTime.now());
        dept.setUpdatedBy(SecurityUtils.getUserId());
        dept.setUpdatedAt(LocalDateTime.now());

        int rows = sysDeptMapper.insert(dept);
        if (rows <= 0) {
            throw new BusinessException("新增部门失败");
        }
    }

    @Override
    public void updateDept(Long id, DeptUpdateDTO dto) {
        SysDept dept = sysDeptMapper.selectById(id);
        if (dept == null || dept.getDeleted() != 0) {
            throw new BusinessException("部门不存在");
        }

        if (Objects.equals(id, dto.getParentId())) {
            throw new BusinessException("父部门不能是自己");
        }

        if (dto.getParentId() != 0) {
            SysDept parent = sysDeptMapper.selectById(dto.getParentId());
            if (parent == null || parent.getDeleted() != 0) {
                throw new BusinessException("父部门不存在");
            }
        }

        SysDept exist = sysDeptMapper.selectByDeptCode(dto.getDeptCode());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException("部门编码已存在");
        }

        dept.setParentId(dto.getParentId());
        dept.setDeptName(dto.getDeptName());
        dept.setDeptCode(dto.getDeptCode());
        dept.setLeaderUserId(dto.getLeaderUserId());
        dept.setSortNum(dto.getSortNum() == null ? 0 : dto.getSortNum());
        dept.setStatus(dto.getStatus());
        dept.setRemark(dto.getRemark());
        dept.setUpdatedBy(SecurityUtils.getUserId());
        dept.setUpdatedAt(LocalDateTime.now());

        int rows = sysDeptMapper.updateById(dept);
        if (rows <= 0) {
            throw new BusinessException("修改部门失败");
        }
    }

    @Override
    public void deleteDept(Long id) {
        SysDept dept = sysDeptMapper.selectById(id);
        if (dept == null || dept.getDeleted() != 0) {
            throw new BusinessException("部门不存在");
        }

        Long childCount = sysDeptMapper.countChildrenByDeptId(id);
        if (childCount != null && childCount > 0) {
            throw new BusinessException("存在子部门，不能删除");
        }

        dept.setDeleted(1);
        dept.setUpdatedBy(SecurityUtils.getUserId());
        dept.setUpdatedAt(LocalDateTime.now());

        int rows = sysDeptMapper.updateById(dept);
        if (rows <= 0) {
            throw new BusinessException("删除部门失败");
        }
    }

    private DeptTreeVO toVO(SysDept dept) {
        DeptTreeVO vo = new DeptTreeVO();
        vo.setId(dept.getId());
        vo.setParentId(dept.getParentId());
        vo.setDeptName(dept.getDeptName());
        vo.setDeptCode(dept.getDeptCode());
        vo.setLeaderUserId(dept.getLeaderUserId());
        vo.setSortNum(dept.getSortNum());
        vo.setStatus(dept.getStatus());
        vo.setRemark(dept.getRemark());
        return vo;
    }

    private List<DeptTreeVO> buildTree(List<DeptTreeVO> all, Long parentId) {
        List<DeptTreeVO> result = new ArrayList<>();
        for (DeptTreeVO item : all) {
            Long pid = item.getParentId() == null ? 0L : item.getParentId();
            if (Objects.equals(pid, parentId)) {
                item.setChildren(buildTree(all, item.getId()));
                result.add(item);
            }
        }
        result.sort(Comparator.comparing(DeptTreeVO::getSortNum, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(DeptTreeVO::getId));
        return result;
    }
}