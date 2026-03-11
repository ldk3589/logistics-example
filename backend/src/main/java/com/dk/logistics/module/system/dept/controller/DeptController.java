package com.dk.logistics.module.system.dept.controller;

import com.dk.logistics.common.api.ApiResult;
import com.dk.logistics.framework.annotation.OperationLog;
import com.dk.logistics.module.system.dept.dto.DeptAddDTO;
import com.dk.logistics.module.system.dept.dto.DeptUpdateDTO;
import com.dk.logistics.module.system.dept.service.DeptService;
import com.dk.logistics.module.system.dept.vo.DeptTreeVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/depts")
public class DeptController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('sys:dept:list')")
    public ApiResult<List<DeptTreeVO>> listDeptTree() {
        return ApiResult.success(deptService.listDeptTree());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dept:list')")
    public ApiResult<DeptTreeVO> getDeptById(@PathVariable("id") Long id) {
        return ApiResult.success(deptService.getDeptById(id));
    }

    @OperationLog(module = "部门管理", name = "新增部门")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:dept:add')")
    public ApiResult<Void> addDept(@Valid @RequestBody DeptAddDTO dto) {
        deptService.addDept(dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "部门管理", name = "修改部门")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dept:update')")
    public ApiResult<Void> updateDept(@PathVariable("id") Long id,
                                      @Valid @RequestBody DeptUpdateDTO dto) {
        deptService.updateDept(id, dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "部门管理", name = "删除部门")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dept:delete')")
    public ApiResult<Void> deleteDept(@PathVariable("id") Long id) {
        deptService.deleteDept(id);
        return ApiResult.success(null);
    }
}