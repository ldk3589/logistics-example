package com.dk.logistics.enums;

public enum OrderStatus {
    CREATED,     // 用户创建
    ASSIGNED,    // 一级管理员已指派
    PROCESSING,  // 二级管理员处理中
    COMPLETED,   // 完成
    CANCELLED    // 取消
}

