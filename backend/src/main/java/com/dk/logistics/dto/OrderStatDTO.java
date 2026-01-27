package com.dk.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderStatDTO {
    private String period;     // 时间段，如 "2026-W03" / "2026-01" / "2026"
    private Integer count;     // 订单数量
    private BigDecimal totalAmount; // 订单总金额
}
