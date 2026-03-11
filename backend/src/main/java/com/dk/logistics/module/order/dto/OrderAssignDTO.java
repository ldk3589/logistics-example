package com.dk.logistics.module.order.dto;

import jakarta.validation.constraints.NotNull;

public class OrderAssignDTO {

    @NotNull(message = "负责人ID不能为空")
    private Long ownerUserId;

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
}