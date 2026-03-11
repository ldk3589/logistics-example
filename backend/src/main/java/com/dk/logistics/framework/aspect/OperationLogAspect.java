package com.dk.logistics.framework.aspect;

import com.dk.logistics.common.utils.IpUtils;
import com.dk.logistics.common.utils.SecurityUtils;
import com.dk.logistics.framework.annotation.OperationLog;
import com.dk.logistics.module.system.log.entity.SysOperationLog;
import com.dk.logistics.module.system.log.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {

    private final LogService logService;
    private final ObjectMapper objectMapper;

    public OperationLogAspect(LogService logService, ObjectMapper objectMapper) {
        this.logService = logService;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        long start = System.currentTimeMillis();

        SysOperationLog log = new SysOperationLog();
        log.setModuleName(operationLog.module());
        log.setOperationName(operationLog.name());
        log.setOperatorUserId(SecurityUtils.getUserId());
        log.setOperatorName(SecurityUtils.getUsername());
        log.setCreatedAt(LocalDateTime.now());

        HttpServletRequest request = getRequest();
        if (request != null) {
            log.setRequestMethod(request.getMethod());
            log.setRequestUri(request.getRequestURI());
            log.setOperatorIp(IpUtils.getIpAddress(request));
        }

        try {
            String params = objectMapper.writeValueAsString(joinPoint.getArgs());
            log.setRequestParams(limitText(params, 2000));
        } catch (Exception e) {
            log.setRequestParams("[参数序列化失败]");
        }

        try {
            Object result = joinPoint.proceed();
            log.setOperationStatus(1);
            try {
                log.setResponseData(limitText(objectMapper.writeValueAsString(result), 2000));
            } catch (Exception e) {
                log.setResponseData("[返回结果序列化失败]");
            }
            return result;
        } catch (Throwable e) {
            log.setOperationStatus(0);
            log.setErrorMessage(limitText(e.getMessage(), 2000));
            throw e;
        } finally {
            log.setCostTimeMs(System.currentTimeMillis() - start);
            logService.saveOperationLog(log);
        }
    }

    private HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    private String limitText(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        return text.length() <= maxLength ? text : text.substring(0, maxLength);
    }
}