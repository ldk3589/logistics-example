package com.dk.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dk.logistics.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface BillMapper extends BaseMapper<Bill> {

    // 统计某天订单数
    @Select("SELECT COUNT(*) FROM bill WHERE user_id = #{userId} AND DATE(created_at) = DATE(#{date})")
    long countBillsByDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    // 统计某天总金额
    @Select("SELECT SUM(amount) FROM bill WHERE user_id = #{userId} AND DATE(created_at) = DATE(#{date})")
    BigDecimal sumBillsByDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    // 统计某月订单数
    @Select("SELECT COUNT(*) FROM bill WHERE user_id = #{userId} AND YEAR(created_at) = #{year} AND MONTH(created_at) = #{month}")
    long countBillsByMonth(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    // 统计某月总金额
    @Select("SELECT SUM(amount) FROM bill WHERE user_id = #{userId} AND YEAR(created_at) = #{year} AND MONTH(created_at) = #{month}")
    BigDecimal sumBillsByMonth(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);
}