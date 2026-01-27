package com.dk.logistics.controller;


import com.dk.logistics.entity.SysUser;
import com.dk.logistics.enums.StatPeriod;
import com.dk.logistics.service.StatService;
import com.dk.logistics.utils.SecurityUtil;
import com.dk.logistics.vo.OrderTrendVO;
import com.dk.logistics.vo.StatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping("/orders")
    public StatVO orderStat(@RequestParam("period") StatPeriod period) {
        SysUser user = SecurityUtil.getCurrentUser();
        return statService.orderSummary(user, period);
    }

    @GetMapping("/orders/trend")
    public OrderTrendVO orderTrend(@RequestParam("period") StatPeriod period) {
        SysUser user = SecurityUtil.getCurrentUser();
        return statService.getOrderTrend(user, period); // 需要在 StatService 中实现
    }
}
