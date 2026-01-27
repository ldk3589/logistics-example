package com.dk.logistics.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatVO {
    private BigDecimal totalAmount;
    private Integer orderCount;
}
