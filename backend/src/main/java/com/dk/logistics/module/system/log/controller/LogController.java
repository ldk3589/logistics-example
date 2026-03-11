package com.dk.logistics.module.system.log.controller;

import com.dk.logistics.common.api.ApiResult;
import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.module.system.log.dto.LoginLogQueryDTO;
import com.dk.logistics.module.system.log.dto.OperationLogQueryDTO;
import com.dk.logistics.module.system.log.entity.SysLoginLog;
import com.dk.logistics.module.system.log.entity.SysOperationLog;
import com.dk.logistics.module.system.log.service.LogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/operations")
    @PreAuthorize("hasAuthority('sys:oplog:list')")
    public ApiResult<PageResult<SysOperationLog>> listOperationLogs(OperationLogQueryDTO queryDTO) {
        return ApiResult.success(logService.listOperationLogs(queryDTO));
    }

    @GetMapping("/logins")
    @PreAuthorize("hasAuthority('sys:loginlog:list')")
    public ApiResult<PageResult<SysLoginLog>> listLoginLogs(LoginLogQueryDTO queryDTO) {
        return ApiResult.success(logService.listLoginLogs(queryDTO));
    }
}