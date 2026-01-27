package com.dk.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    // 使用注解快速实现关联查询
    @Select("SELECT o.*, u.username as adminL2Name " +
            "FROM orders o " +
            "LEFT JOIN sys_user u ON o.admin_l2_id = u.id " +
            "ORDER BY o.created_at DESC")
    List<Order> selectOrdersWithAdminName();
}