package com.dk.logistics.module.auth.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatVO {
    private BigDecimal totalAmount;
    private Integer orderCount;
}
