package com.dk.logistics.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dk.logistics.entity.Bill;
import com.dk.logistics.entity.SysUser;
import com.dk.logistics.enums.StatPeriod;
import com.dk.logistics.mapper.BillMapper;
import com.dk.logistics.service.StatService;
import com.dk.logistics.vo.OrderTrendVO;
import com.dk.logistics.vo.StatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private BillMapper billMapper;

    @Override
    public StatVO orderSummary(SysUser user, StatPeriod period) {

        LocalDateTime start = getStartTime(period);

        LambdaQueryWrapper<Bill> qw = new LambdaQueryWrapper<>();
        qw.ge(start != null, Bill::getCreatedAt, start);

        // 🔐 权限控制
        switch (user.getRole()) {
            case "USER" ->
                    qw.eq(Bill::getUserId, user.getId());

            case "ADMIN_L2" ->
                    qw.inSql(
                            Bill::getUserId,
                            "SELECT id FROM sys_user WHERE parent_admin_id = " + user.getId()
                    );

            case "ADMIN_L1" -> {
                // 不加条件
            }
        }

        List<Bill> bills = billMapper.selectList(qw);

        StatVO vo = new StatVO();
        vo.setOrderCount(bills.size());
        vo.setTotalAmount(
                bills.stream()
                        .map(Bill::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        return vo;
    }

    private LocalDateTime getStartTime(StatPeriod period) {
        return switch (period) {
            case WEEK -> LocalDateTime.now().minusWeeks(1);
            case MONTH -> LocalDateTime.now().minusMonths(1);
            case YEAR -> LocalDateTime.now().minusYears(1);
            case HISTORY -> null;
        };
    }

    @Override
    public OrderTrendVO getOrderTrend(SysUser user, StatPeriod period) {
        OrderTrendVO trendVO = new OrderTrendVO();
        List<String> dates = new ArrayList<>();
        List<Long> orderCounts = new ArrayList<>();
        List<BigDecimal> totalAmounts = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;

        switch (period) {
            case WEEK:
                startDate = now.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                // 模拟数据，实际应查询数据库
                for (int i = 0; i < 7; i++) {
                    LocalDateTime date = startDate.plusDays(i);
                    dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
                    // 假设查询每日数据
                    long count = billMapper.countBillsByDate(user.getId(), date); // 需要自定义 Mapper 方法
                    BigDecimal amount = billMapper.sumBillsByDate(user.getId(), date); // 需要自定义 Mapper 方法
                    orderCounts.add(count);
                    totalAmounts.add(amount != null ? amount : BigDecimal.ZERO);
                }
                break;
            case MONTH:
                startDate = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                for (int i = 0; i < 30; i++) { // 模拟近30天
                    LocalDateTime date = startDate.plusDays(i);
                    dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
                    long count = billMapper.countBillsByDate(user.getId(), date);
                    BigDecimal amount = billMapper.sumBillsByDate(user.getId(), date);
                    orderCounts.add(count);
                    totalAmounts.add(amount != null ? amount : BigDecimal.ZERO);
                }
                break;
            case YEAR:
                startDate = now.minusYears(1).with(TemporalAdjusters.firstDayOfYear());
                for (int i = 0; i < 12; i++) { // 模拟近12个月
                    LocalDateTime date = startDate.plusMonths(i);
                    dates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                    long count = billMapper.countBillsByMonth(user.getId(), date.getYear(), date.getMonthValue());
                    BigDecimal amount = billMapper.sumBillsByMonth(user.getId(), date.getYear(), date.getMonthValue());
                    orderCounts.add(count);
                    totalAmounts.add(amount != null ? amount : BigDecimal.ZERO);
                }
                break;
            default: // HISTORY 或其他，不提供趋势图
                break;
        }

        trendVO.setDates(dates);
        trendVO.setOrderCounts(orderCounts);
        trendVO.setTotalAmounts(totalAmounts);
        return trendVO;
    }
}
