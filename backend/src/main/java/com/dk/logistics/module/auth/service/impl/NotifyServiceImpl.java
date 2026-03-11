package com.dk.logistics.security.service.impl;

import com.dk.logistics.entity.Order;
import com.dk.logistics.entity.SysUser;
import com.dk.logistics.security.service.NotifyService;
import org.springframework.stereotype.Service;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Override
    public void notifyOrderCompleted(Order order, SysUser user) {

        if (user.getEmail() != null) {
            System.out.println("【邮件通知】发送给 "
                    + user.getEmail()
                    + "，订单 " + order.getId() + " 已完成");
        }

        if (user.getPhone() != null) {
            System.out.println("【短信通知】发送给 "
                    + user.getPhone()
                    + "，订单 " + order.getId() + " 已完成");
        }
    }
}
