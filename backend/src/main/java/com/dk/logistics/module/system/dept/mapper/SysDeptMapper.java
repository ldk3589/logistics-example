package com.dk.logistics.module.system.dept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.system.dept.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    @Select("""
        SELECT id, parent_id, dept_name, dept_code, leader_user_id, sort_num,
               status, remark, created_by, created_at, updated_by, updated_at, deleted
        FROM sys_dept
        WHERE deleted = 0
        ORDER BY sort_num ASC, id ASC
        """)
    List<SysDept> selectAllActive();

    @Select("""
        SELECT id, parent_id, dept_name, dept_code, leader_user_id, sort_num,
               status, remark, created_by, created_at, updated_by, updated_at, deleted
        FROM sys_dept
        WHERE dept_code = #{deptCode}
          AND deleted = 0
        LIMIT 1
        """)
    SysDept selectByDeptCode(String deptCode);

    @Select("""
        SELECT COUNT(1)
        FROM sys_dept
        WHERE parent_id = #{deptId}
          AND deleted = 0
        """)
    Long countChildrenByDeptId(Long deptId);

    @Select("""
        SELECT id
        FROM sys_dept
        WHERE deleted = 0
        ORDER BY sort_num ASC, id ASC
        """)
    List<Long> selectAllDeptIds();

    @Select("""
        SELECT id
        FROM sys_dept
        WHERE parent_id = #{deptId}
          AND deleted = 0
        """)
    List<Long> selectChildDeptIds(Long deptId);

    @Select("""
        SELECT id
        FROM sys_dept
        WHERE deleted = 0
        ORDER BY sort_num ASC, id ASC
        """)
    List<Long> selectDeptAndChildrenIds(Long deptId);
}