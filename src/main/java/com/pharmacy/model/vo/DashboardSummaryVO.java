package com.pharmacy.model.vo;

import java.math.BigDecimal;
/**
 * 仪表盘数据统计视图对象
 */
public record DashboardSummaryVO(BigDecimal todaySalesAmount, Long lowStockCount, Long expiringSoonCount,
        Long todayOrderCount) {
}
