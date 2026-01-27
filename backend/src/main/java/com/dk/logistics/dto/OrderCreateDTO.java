package com.dk.logistics.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateDTO {
    private BigDecimal amount;
    private String phone;
    private String email;
    private List<String> notifyMethods;
}
