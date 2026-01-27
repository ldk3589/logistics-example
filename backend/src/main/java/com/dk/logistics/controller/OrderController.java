package com.dk.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dk.logistics.dto.AssignOrderDTO;
import com.dk.logistics.dto.OrderCreateDTO;
import com.dk.logistics.dto.OrderStatDTO;
import com.dk.logistics.entity.Order;
import com.dk.logistics.entity.SysUser;
import com.dk.logistics.mapper.UserMapper;
import com.dk.logistics.service.OrderService;
import com.dk.logistics.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 创建订单
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN_L2','ADMIN_L1')")
    @PostMapping
    public void create(@RequestBody OrderCreateDTO dto) {
        SysUser user = SecurityUtil.getCurrentUser();
        orderService.createOrder(dto, user);
    }

    /**
     * 订单列表（按角色返回不同数据）
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN_L2','ADMIN_L1')")
    @GetMapping
    public List<Order> list() {
        SysUser user = SecurityUtil.getCurrentUser();
        return orderService.listOrders(user);
    }

    /**
     * 删除订单
     */
    @PreAuthorize("hasAnyRole('USER','ADMIN_L2','ADMIN_L1')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        SysUser user = SecurityUtil.getCurrentUser();
        orderService.deleteOrder(id, user);
    }

    /**
     * 修改订单状态（二级 / 一级管理员）
     */
    @PreAuthorize("hasAnyRole('ADMIN_L2','ADMIN_L1')")
    @PutMapping("/{id}/status")
    public void changeStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {

        SysUser user = SecurityUtil.getCurrentUser();
        orderService.changeStatus(id, status, user);
    }

    /**
     * 一级管理员 → 获取二级管理员列表
     */
    @PreAuthorize("hasRole('ADMIN_L1')")
    @GetMapping("/admin-l2-list")
    public List<SysUser> getAdminL2List() {
        // 日志方便排查权限
        System.out.println(">>> getAdminL2List invoked by " + SecurityUtil.getCurrentUser().getUsername());

        return userMapper.selectList(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getRole, "ADMIN_L2")
        );
    }

    /**
     一级管理员 → 指派订单给二级管理员
     */
    @PreAuthorize("hasRole('ADMIN_L1')")
    @PutMapping("/{orderId}/assign")
    public void assignOrder(
            @PathVariable("orderId") Long id,
            @RequestBody AssignOrderDTO dto
    ) {
        SysUser currentUser = SecurityUtil.getCurrentUser();

        // 日志方便排查权限和参数
        log.info("assign order: orderId={}, adminL2Id={}", id, dto.getAdminL2Id());

        orderService.assignOrder(id, dto.getAdminL2Id(), currentUser);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN_L2','ADMIN_L1')")
    @GetMapping("/statistics")
    public List<OrderStatDTO> statistics(@RequestParam("range") String range) {
        SysUser user = SecurityUtil.getCurrentUser();
        return orderService.getStatistics(user, range);
    }

}
