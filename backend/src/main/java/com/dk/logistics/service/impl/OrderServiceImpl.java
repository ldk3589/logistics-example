package com.dk.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dk.logistics.dto.OrderCreateDTO;
import com.dk.logistics.dto.OrderStatDTO;
import com.dk.logistics.entity.Bill;
import com.dk.logistics.entity.Order;
import com.dk.logistics.entity.SysUser;
import com.dk.logistics.mapper.BillMapper;
import com.dk.logistics.mapper.OrderMapper;
import com.dk.logistics.mapper.UserMapper;
import com.dk.logistics.service.NotifyService;
import com.dk.logistics.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private NotifyService notifyService;

    /* ================= 创建订单 ================= */

    @Override
    public void createOrder(OrderCreateDTO dto, SysUser user) {

        Order order = new Order();
        order.setUserId(user.getId());
        order.setAmount(dto.getAmount());
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        order.setPhone(dto.getPhone());
        order.setEmail(dto.getEmail());

        if (dto.getNotifyMethods() != null && !dto.getNotifyMethods().isEmpty()) {
            order.setNotifyMethods(String.join(",", dto.getNotifyMethods()));
        }

        // 普通用户：自动绑定自己的二级管理员
        if ("USER".equals(user.getRole())) {
            order.setAdminL2Id(user.getParentAdminId());
        }

        // 二级管理员自己下单
        if ("ADMIN_L2".equals(user.getRole())) {
            order.setAdminL2Id(user.getId());
        }

        orderMapper.insert(order);
    }

    /* ================= 查询订单 ================= */

    @Override
    public List<Order> listOrders(SysUser user) {
        LambdaQueryWrapper<Order> qw = new LambdaQueryWrapper<>();

        // 1. 根据角色设置过滤条件
        switch (user.getRole()) {
            case "USER" ->
                // 普通用户：只能看自己下的单
                    qw.eq(Order::getUserId, user.getId());

            case "ADMIN_L2" ->
                // 二级管理员：看自己下的单 (userId) OR 自己负责的单 (adminL2Id)
                    qw.and(wrapper -> wrapper
                            .eq(Order::getUserId, user.getId())
                            .or()
                            .eq(Order::getAdminL2Id, user.getId())
                    );

            case "ADMIN_L1" -> {
                // 一级管理员：不加过滤条件，查看全部
            }

            default -> throw new RuntimeException("未知角色权限");
        }

        // 2. 统一按创建时间倒序排列
        qw.orderByDesc(Order::getCreatedAt);

        // 3. 执行基础查询
        List<Order> orders = orderMapper.selectList(qw);

        // 4. 填充关联的名称（UserName 和 AdminL2Name）
        // 这里的填充逻辑确保前端 el-table-column 能拿到 row.userName 和 row.adminL2Name
        for (Order order : orders) {
            // 填充下单人姓名
            if (order.getUserId() != null) {
                SysUser u = userMapper.selectById(order.getUserId());
                if (u != null) order.setUserName(u.getUsername());
            }
            // 填充负责的管理员姓名
            if (order.getAdminL2Id() != null) {
                SysUser a = userMapper.selectById(order.getAdminL2Id());
                if (a != null) order.setAdminL2Name(a.getUsername());
            }
        }

        return orders;
    }

    /* ================= 删除订单 ================= */

    @Override
    public void deleteOrder(Long orderId, SysUser user) {

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if ("USER".equals(user.getRole())
                && !order.getUserId().equals(user.getId())) {
            throw new RuntimeException("无权限删除该订单");
        }

        if ("ADMIN_L2".equals(user.getRole())
                && !user.getId().equals(order.getAdminL2Id())) {
            throw new RuntimeException("无权限删除该订单");
        }

        orderMapper.deleteById(orderId);
    }

    /* ================= 修改状态 ================= */

    @Override
    @Transactional
    public void changeStatus(Long orderId, String status, SysUser user) {

        if ("USER".equals(user.getRole())) {
            throw new RuntimeException("普通用户不能修改订单状态");
        }

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if ("COMPLETED".equals(order.getStatus())) {
            throw new RuntimeException("订单已完成，不能重复操作");
        }

        if ("COMPLETED".equals(status)) {

            order.setStatus("COMPLETED");
            order.setCompletedAt(LocalDateTime.now());
            orderMapper.updateById(order);

            Bill bill = new Bill();
            bill.setOrderId(order.getId());
            bill.setUserId(order.getUserId());
            bill.setAmount(order.getAmount());
            bill.setCreatedAt(LocalDateTime.now());
            billMapper.insert(bill);

            SysUser orderUser = userMapper.selectById(order.getUserId());
            notifyService.notifyOrderCompleted(order, orderUser);

        } else {
            order.setStatus(status);
            orderMapper.updateById(order);
        }
    }

    /* ================= ⭐ 指派订单（关键修复） ================= */

    @Override
    @Transactional
    public void assignOrder(Long orderId, Long adminL2Id, SysUser currentUser) {

        if (!"ADMIN_L1".equals(currentUser.getRole())) {
            throw new RuntimeException("无权限指派订单");
        }


        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("当前状态不可指派");
        }

        SysUser adminL2 = userMapper.selectById(adminL2Id);
        if (adminL2 == null || !"ADMIN_L2".equals(adminL2.getRole())) {
            throw new RuntimeException("只能指派给二级管理员");
        }

        order.setAdminL2Id(adminL2Id);
        order.setStatus("ASSIGNED");


        orderMapper.updateById(order);
    }

    @Override
    public List<OrderStatDTO> getStatistics(SysUser user, String range) {
        // 根据角色选择范围
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (user.getRole().equals("USER")) {
            wrapper.eq(Order::getUserId, user.getId());
        } else if (user.getRole().equals("ADMIN_L2")) {
            wrapper.eq(Order::getAdminL2Id, user.getId());
        } // ADMIN_L1 不限制

        // 根据 range 设置时间范围
        LocalDate now = LocalDate.now();
        switch (range) {
            case "week":
                wrapper.ge(Order::getCreatedAt, now.with(DayOfWeek.MONDAY));
                break;
            case "month":
                wrapper.ge(Order::getCreatedAt, now.withDayOfMonth(1));
                break;
            case "year":
                wrapper.ge(Order::getCreatedAt, now.withDayOfYear(1));
                break;
            case "all":
            default:
                break;
        }

        List<Order> orders = orderMapper.selectList(wrapper);

        // 按时间段统计
        Map<String, OrderStatDTO> map = new TreeMap<>();

        for (Order order : orders) {
            String key;
            switch (range) {
                case "week":
                    key = order.getCreatedAt().getDayOfWeek().toString();
                    break;
                case "month":
                    key = String.valueOf(order.getCreatedAt().getDayOfMonth());
                    break;
                case "year":
                    key = String.valueOf(order.getCreatedAt().getMonthValue());
                    break;
                case "all":
                default:
                    key = String.valueOf(order.getCreatedAt().getYear());
                    break;
            }
            map.compute(key, (k, v) -> {
                if (v == null) return new OrderStatDTO(k, 1, order.getAmount());
                v.setCount(v.getCount() + 1);
                v.setTotalAmount(v.getTotalAmount().add(order.getAmount()));
                return v;
            });
        }

        return new ArrayList<>(map.values());
    }
}
