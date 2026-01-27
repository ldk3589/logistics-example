package com.dk.logistics.service;

import com.dk.logistics.dto.OrderCreateDTO;
import com.dk.logistics.dto.OrderStatDTO;
import com.dk.logistics.entity.Order;
import com.dk.logistics.entity.SysUser;

import java.util.List;

public interface OrderService {

    void createOrder(OrderCreateDTO dto, SysUser currentUser);

    List<Order> listOrders(SysUser currentUser);

    void deleteOrder(Long orderId, SysUser currentUser);

    void changeStatus(Long orderId, String status, SysUser currentUser);

    void assignOrder(Long orderId, Long adminL2Id, SysUser currentUser);

    List<OrderStatDTO> getStatistics(SysUser user, String range);
}
