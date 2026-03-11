package com.dk.logistics.module.system.dept.service;

import com.dk.logistics.module.system.dept.dto.DeptAddDTO;
import com.dk.logistics.module.system.dept.dto.DeptUpdateDTO;
import com.dk.logistics.module.system.dept.vo.DeptTreeVO;

import java.util.List;

public interface DeptService {

    List<DeptTreeVO> listDeptTree();

    DeptTreeVO getDeptById(Long id);

    void addDept(DeptAddDTO dto);

    void updateDept(Long id, DeptUpdateDTO dto);

    void deleteDept(Long id);
}