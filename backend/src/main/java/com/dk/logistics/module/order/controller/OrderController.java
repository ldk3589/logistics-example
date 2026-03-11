package com.dk.logistics.module.order.controller;

import com.dk.logistics.common.api.ApiResult;
import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.framework.annotation.OperationLog;
import com.dk.logistics.module.order.dto.OrderAddDTO;
import com.dk.logistics.module.order.dto.OrderAssignDTO;
import com.dk.logistics.module.order.dto.OrderQueryDTO;
import com.dk.logistics.module.order.dto.OrderUpdateDTO;
import com.dk.logistics.module.order.service.OrderService;
import com.dk.logistics.module.order.vo.OrderVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('order:list')")
    public ApiResult<PageResult<OrderVO>> listOrders(OrderQueryDTO queryDTO) {
        return ApiResult.success(orderService.listOrders(queryDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('order:list')")
    public ApiResult<OrderVO> getOrderById(@PathVariable("id") Long id) {
        return ApiResult.success(orderService.getOrderById(id));
    }

    @OperationLog(module = "订单管理", name = "新增订单")
    @PostMapping
    @PreAuthorize("hasAuthority('order:add')")
    public ApiResult<Void> addOrder(@Valid @RequestBody OrderAddDTO dto) {
        orderService.addOrder(dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "订单管理", name = "修改订单")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('order:update')")
    public ApiResult<Void> updateOrder(@PathVariable("id") Long id,
                                       @Valid @RequestBody OrderUpdateDTO dto) {
        orderService.updateOrder(id, dto);
        return ApiResult.success(null);
    }

    @OperationLog(module = "订单管理", name = "删除订单")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('order:delete')")
    public ApiResult<Void> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ApiResult.success(null);
    }

    @OperationLog(module = "订单管理", name = "分配订单")
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('order:assign')")
    public ApiResult<Void> assignOrder(@PathVariable("id") Long id,
                                       @Valid @RequestBody OrderAssignDTO dto) {
        orderService.assignOrder(id, dto.getOwnerUserId());
        return ApiResult.success(null);
    }

    @OperationLog(module = "订单管理", name = "完成订单")
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('order:update')")
    public ApiResult<Void> completeOrder(@PathVariable("id") Long id) {
        orderService.completeOrder(id);
        return ApiResult.success(null);
    }
}