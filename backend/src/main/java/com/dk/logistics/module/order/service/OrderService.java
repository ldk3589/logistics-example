package com.dk.logistics.module.order.service;

import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.module.order.dto.OrderAddDTO;
import com.dk.logistics.module.order.dto.OrderAssignDTO;
import com.dk.logistics.module.order.dto.OrderQueryDTO;
import com.dk.logistics.module.order.dto.OrderUpdateDTO;
import com.dk.logistics.module.order.vo.OrderVO;

public interface OrderService {
    PageResult<OrderVO> listOrders(OrderQueryDTO queryDTO);

    OrderVO getOrderById(Long id);

    void addOrder(OrderAddDTO dto);

    void updateOrder(Long id, OrderUpdateDTO dto);

    void deleteOrder(Long id);

    void assignOrder(Long id, Long ownerUserId);

    void completeOrder(Long id);
}