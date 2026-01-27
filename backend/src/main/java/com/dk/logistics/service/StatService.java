package com.dk.logistics.service;

import com.dk.logistics.entity.SysUser;
import com.dk.logistics.enums.StatPeriod;
import com.dk.logistics.vo.OrderTrendVO;
import com.dk.logistics.vo.StatVO;

public interface StatService {
    StatVO orderSummary(SysUser user, StatPeriod period);
    OrderTrendVO getOrderTrend(SysUser user, StatPeriod period); // 新增
}