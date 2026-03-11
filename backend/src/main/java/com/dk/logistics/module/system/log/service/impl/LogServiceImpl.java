package com.dk.logistics.module.system.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.module.system.log.dto.LoginLogQueryDTO;
import com.dk.logistics.module.system.log.dto.OperationLogQueryDTO;
import com.dk.logistics.module.system.log.entity.SysLoginLog;
import com.dk.logistics.module.system.log.entity.SysOperationLog;
import com.dk.logistics.module.system.log.mapper.SysLoginLogMapper;
import com.dk.logistics.module.system.log.mapper.SysOperationLogMapper;
import com.dk.logistics.module.system.log.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LogServiceImpl implements LogService {

    private final SysOperationLogMapper sysOperationLogMapper;
    private final SysLoginLogMapper sysLoginLogMapper;

    public LogServiceImpl(SysOperationLogMapper sysOperationLogMapper,
                          SysLoginLogMapper sysLoginLogMapper) {
        this.sysOperationLogMapper = sysOperationLogMapper;
        this.sysLoginLogMapper = sysLoginLogMapper;
    }

    @Override
    public void saveOperationLog(SysOperationLog log) {
        sysOperationLogMapper.insert(log);
    }

    @Override
    public void saveLoginLog(SysLoginLog log) {
        sysLoginLogMapper.insert(log);
    }

    @Override
    public PageResult<SysOperationLog> listOperationLogs(OperationLogQueryDTO queryDTO) {
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getModuleName())) {
                wrapper.like(SysOperationLog::getModuleName, queryDTO.getModuleName());
            }
            if (StringUtils.hasText(queryDTO.getOperationName())) {
                wrapper.like(SysOperationLog::getOperationName, queryDTO.getOperationName());
            }
            if (queryDTO.getOperationStatus() != null) {
                wrapper.eq(SysOperationLog::getOperationStatus, queryDTO.getOperationStatus());
            }
            if (StringUtils.hasText(queryDTO.getOperatorName())) {
                wrapper.like(SysOperationLog::getOperatorName, queryDTO.getOperatorName());
            }
        }

        wrapper.orderByDesc(SysOperationLog::getId);

        Page<SysOperationLog> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<SysOperationLog> resultPage = sysOperationLogMapper.selectPage(page, wrapper);

        return new PageResult<>(resultPage.getTotal(), resultPage.getRecords());
    }

    @Override
    public PageResult<SysLoginLog> listLoginLogs(LoginLogQueryDTO queryDTO) {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getUsername())) {
                wrapper.like(SysLoginLog::getUsername, queryDTO.getUsername());
            }
            if (queryDTO.getLoginStatus() != null) {
                wrapper.eq(SysLoginLog::getLoginStatus, queryDTO.getLoginStatus());
            }
        }

        wrapper.orderByDesc(SysLoginLog::getId);

        Page<SysLoginLog> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<SysLoginLog> resultPage = sysLoginLogMapper.selectPage(page, wrapper);

        return new PageResult<>(resultPage.getTotal(), resultPage.getRecords());
    }
}