package com.dk.logistics.module.auth.service;


import com.dk.logistics.module.order.entity.Order;
import com.dk.logistics.module.system.user.entity.SysUser;

public interface NotifyService {
    void notifyOrderCompleted(Order order, SysUser user);
}
