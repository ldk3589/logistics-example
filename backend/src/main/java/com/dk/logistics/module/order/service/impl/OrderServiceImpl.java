package com.dk.logistics.module.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dk.logistics.common.api.PageResult;
import com.dk.logistics.common.constant.DataScopeConstants;
import com.dk.logistics.common.exception.BusinessException;
import com.dk.logistics.common.utils.SecurityUtils;
import com.dk.logistics.module.order.dto.OrderAddDTO;
import com.dk.logistics.module.order.dto.OrderQueryDTO;
import com.dk.logistics.module.order.dto.OrderUpdateDTO;
import com.dk.logistics.module.order.entity.Order;
import com.dk.logistics.module.order.mapper.OrderMapper;
import com.dk.logistics.module.order.service.OrderService;
import com.dk.logistics.module.order.vo.OrderVO;
import com.dk.logistics.module.system.dept.mapper.SysDeptMapper;
import com.dk.logistics.module.system.role.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysDeptMapper sysDeptMapper;

    public OrderServiceImpl(OrderMapper orderMapper,
                            SysRoleMapper sysRoleMapper,
                            SysDeptMapper sysDeptMapper) {
        this.orderMapper = orderMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysDeptMapper = sysDeptMapper;
    }

    @Override
    public PageResult<OrderVO> listOrders(OrderQueryDTO queryDTO) {
        Long userId = SecurityUtils.getUserId();
        Long deptId = SecurityUtils.getDeptId();

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.hasText(queryDTO.getOrderNo())) {
                wrapper.like(Order::getOrderNo, queryDTO.getOrderNo());
            }
            if (StringUtils.hasText(queryDTO.getCustomerName())) {
                wrapper.like(Order::getCustomerName, queryDTO.getCustomerName());
            }
            if (StringUtils.hasText(queryDTO.getStatus())) {
                wrapper.eq(Order::getStatus, queryDTO.getStatus());
            }
        }

        List<String> dataScopes = sysRoleMapper.selectDataScopesByUserId(userId);

        if (dataScopes.contains(DataScopeConstants.ALL)) {
            // 不限制
        } else if (dataScopes.contains(DataScopeConstants.DEPT_AND_CHILD)) {
            List<Long> deptIds = sysDeptMapper.selectDeptAndChildrenIds(deptId);
            if (deptIds == null || deptIds.isEmpty()) {
                wrapper.eq(Order::getDeptId, -1L);
            } else {
                wrapper.in(Order::getDeptId, deptIds);
            }
        } else if (dataScopes.contains(DataScopeConstants.DEPT)) {
            wrapper.eq(Order::getDeptId, deptId);
        } else {
            wrapper.eq(Order::getOwnerUserId, userId);
        }

        wrapper.orderByAsc(Order::getId);

        Page<Order> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<Order> resultPage = orderMapper.selectPage(page, wrapper);

        List<OrderVO> records = resultPage.getRecords()
                .stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return new PageResult<>(resultPage.getTotal(), records);
    }

    @Override
    public OrderVO getOrderById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null || order.getDeleted() != 0) {
            throw new BusinessException("订单不存在");
        }
        return toVO(order);
    }

    @Override
    public void addOrder(OrderAddDTO dto) {
        Order exist = orderMapper.selectByOrderNo(dto.getOrderNo());
        if (exist != null) {
            throw new BusinessException("订单号已存在");
        }

        Long userId = SecurityUtils.getUserId();

        Order order = new Order();
        order.setOrderNo(dto.getOrderNo());
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setSourceAddress(dto.getSourceAddress());
        order.setTargetAddress(dto.getTargetAddress());
        order.setStatus("CREATED");
        order.setAmount(dto.getAmount());
        order.setDeptId(dto.getDeptId());
        order.setOwnerUserId(userId);
        order.setCreatedBy(userId);
        order.setUpdatedBy(userId);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDeleted(0);

        int rows = orderMapper.insert(order);
        if (rows <= 0) {
            throw new BusinessException("新增订单失败");
        }
    }

    @Override
    public void updateOrder(Long id, OrderUpdateDTO dto) {
        Order order = orderMapper.selectById(id);
        if (order == null || order.getDeleted() != 0) {
            throw new BusinessException("订单不存在");
        }

        order.setCustomerName(dto.getCustomerName());
        order.setCustomerPhone(dto.getCustomerPhone());
        order.setSourceAddress(dto.getSourceAddress());
        order.setTargetAddress(dto.getTargetAddress());
        order.setStatus(dto.getStatus());
        order.setAmount(dto.getAmount());
        order.setDeptId(dto.getDeptId());
        order.setUpdatedBy(SecurityUtils.getUserId());
        order.setUpdatedAt(LocalDateTime.now());

        int rows = orderMapper.updateById(order);
        if (rows <= 0) {
            throw new BusinessException("修改订单失败");
        }
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null || order.getDeleted() != 0) {
            throw new BusinessException("订单不存在");
        }

        order.setDeleted(1);
        order.setUpdatedBy(SecurityUtils.getUserId());
        order.setUpdatedAt(LocalDateTime.now());

        int rows = orderMapper.updateById(order);
        if (rows <= 0) {
            throw new BusinessException("删除订单失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignOrder(Long id, Long ownerUserId) {
        Order order = orderMapper.selectById(id);
        if (order == null || order.getDeleted() != 0) {
            throw new BusinessException("订单不存在");
        }

        order.setOwnerUserId(ownerUserId);
        order.setStatus("ASSIGNED");
        order.setUpdatedBy(SecurityUtils.getUserId());
        order.setUpdatedAt(LocalDateTime.now());

        int rows = orderMapper.updateById(order);
        if (rows <= 0) {
            throw new BusinessException("分配订单失败");
        }
    }

    @Override
    public void completeOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null || order.getDeleted() != 0) {
            throw new BusinessException("订单不存在");
        }

        order.setStatus("FINISHED");
        order.setUpdatedBy(SecurityUtils.getUserId());
        order.setUpdatedAt(LocalDateTime.now());

        int rows = orderMapper.updateById(order);
        if (rows <= 0) {
            throw new BusinessException("完成订单失败");
        }
    }

    private OrderVO toVO(Order order) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setCustomerName(order.getCustomerName());
        vo.setCustomerPhone(order.getCustomerPhone());
        vo.setSourceAddress(order.getSourceAddress());
        vo.setTargetAddress(order.getTargetAddress());
        vo.setStatus(order.getStatus());
        vo.setAmount(order.getAmount());
        vo.setDeptId(order.getDeptId());
        vo.setOwnerUserId(order.getOwnerUserId());
        return vo;
    }
}