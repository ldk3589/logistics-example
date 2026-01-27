package com.dk.logistics.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;      // 下单用户

    @TableField(exist = false)
    private String adminL2Name;

    @TableField(exist = false)
    private String userName;

    private Long adminL2Id;   // 负责的Ⅱ级管理员

    private BigDecimal amount;

    private String status;    // CREATED / PROCESSING / COMPLETED / CANCELLED

    private String phone;          // 联系电话
    private String email;          // 邮箱
    private String notifyMethods;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
