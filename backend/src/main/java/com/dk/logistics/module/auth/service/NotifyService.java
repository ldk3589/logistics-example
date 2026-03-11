package com.dk.logistics.security.service;

import com.dk.logistics.entity.Order;
import com.dk.logistics.entity.SysUser;

public interface NotifyService {
    void notifyOrderCompleted(Order order, SysUser user);
}
