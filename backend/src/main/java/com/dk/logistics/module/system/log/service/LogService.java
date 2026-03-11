package com.dk.logistics.module.system.log.service;

import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.module.system.log.dto.LoginLogQueryDTO;
import com.dk.logistics.module.system.log.dto.OperationLogQueryDTO;
import com.dk.logistics.module.system.log.entity.SysLoginLog;
import com.dk.logistics.module.system.log.entity.SysOperationLog;

public interface LogService {
    void saveOperationLog(SysOperationLog log);

    void saveLoginLog(SysLoginLog log);

    PageResult<SysOperationLog> listOperationLogs(OperationLogQueryDTO queryDTO);

    PageResult<SysLoginLog> listLoginLogs(LoginLogQueryDTO queryDTO);
}