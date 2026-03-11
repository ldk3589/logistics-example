package com.dk.logistics.module.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.module.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("""
        SELECT id, order_no, customer_name, customer_phone, source_address, target_address,
               status, amount, dept_id, owner_user_id, created_by, updated_by,
               created_at, updated_at, deleted
        FROM orders
        WHERE order_no = #{orderNo}
          AND deleted = 0
        LIMIT 1
        """)
    Order selectByOrderNo(String orderNo);
}