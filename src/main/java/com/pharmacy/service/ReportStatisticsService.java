package com.pharmacy.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 统计报表服务
 */
public interface ReportStatisticsService {

    /**
     * 获取库存总价值统计（按分类）
     *
     * @return 各类别库存价值和占比
     */
    List<Map<String, Object>> getInventoryValueByCategory();

    /**
     * 获取库存数量分布（按分类）
     *
     * @return 各类别库存数量分布
     */
    List<Map<String, Object>> getInventoryQuantityByCategory();

    /**
     * 获取临期药品统计列表
     *
     * @param days 天数（默认 30 天）
     * @return 临期药品列表
     */
    List<Map<String, Object>> getExpiringMedicines(Integer days);

    /**
     * 获取缺货药品统计
     *
     * @return 缺货药品列表
     */
    List<Map<String, Object>> getShortageMedicines();

    /**
     * 获取按日销售统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日销售数据
     */
    List<Map<String, Object>> getDailySalesStats(String startDate, String endDate);

    /**
     * 获取按月销售统计
     *
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 每月销售数据
     */
    List<Map<String, Object>> getMonthlySalesStats(String startMonth, String endMonth);

    /**
     * 获取按年销售统计
     *
     * @param year 年份
     * @return 年度销售数据
     */
    Map<String, Object> getYearlySalesStats(Integer year);

    /**
     * 获取药品销售排行
     *
     * @param limit 返回数量限制
     * @param sortBy 排序字段（quantity-销售量，amount-销售额）
     * @return 销售排行列表
     */
    List<Map<String, Object>> getSalesRanking(Integer limit, String sortBy);

    /**
     * 获取首页仪表盘核心指标
     *
     * @return 仪表盘数据
     */
    Map<String, Object> getDashboardMetrics();

    /**
     * 获取销售趋势数据（折线图）
     *
     * @param days 天数（7 或 30）
     * @return 销售趋势数据
     */
    List<Map<String, Object>> getSalesTrend(Integer days);

    /**
     * 获取库存分类占比数据（饼图）
     *
     * @return 库存分类占比
     */
    List<Map<String, Object>> getCategoryInventoryPieData();

    /**
     * 获取出入库流水趋势
     *
     * @param days 天数
     * @return 出入库趋势数据
     */
    List<Map<String, Object>> getInOutFlowTrend(Integer days);

    /**
     * 导出统计报表为 Excel
     *
     * @param type 报表类型（inventory-库存，sales-销售）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel 数据列表
     */
    List<Map<String, Object>> exportReport(String type, String startDate, String endDate);
}
