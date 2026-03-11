package com.dk.logistics.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderTrendVO {
    private List<String> dates;     // 日期列表，如 ["2026-01-20", "2026-01-21"]
    private List<Long> orderCounts; // 每日/月/周 订单数量
    private List<BigDecimal> totalAmounts; // 每日/月/周 订单金额
}