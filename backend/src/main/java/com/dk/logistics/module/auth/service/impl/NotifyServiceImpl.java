package com.dk.logistics.module.auth.service.impl;


import com.dk.logistics.module.auth.service.NotifyService;
import com.dk.logistics.module.order.entity.Order;
import com.dk.logistics.module.system.user.entity.SysUser;
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
