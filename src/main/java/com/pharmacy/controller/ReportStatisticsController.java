package com.pharmacy.controller;

import com.pharmacy.common.BaseResponse;
import com.pharmacy.common.ResultUtils;
import com.pharmacy.service.ReportStatisticsService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.pharmacy.utils.ExcelUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计报表控制器
 */
@RestController
@RequestMapping("/report")
public class ReportStatisticsController {

    @Resource
    private ReportStatisticsService reportStatisticsService;

    /**
     * 获取库存总价值统计（按分类）
     */
    @GetMapping("/inventory/value")
    public BaseResponse<List<Map<String, Object>>> getInventoryValueByCategory() {
        return ResultUtils.success(reportStatisticsService.getInventoryValueByCategory());
    }

    /**
     * 获取库存数量分布（按分类）
     */
    @GetMapping("/inventory/quantity")
    public BaseResponse<List<Map<String, Object>>> getInventoryQuantityByCategory() {
        return ResultUtils.success(reportStatisticsService.getInventoryQuantityByCategory());
    }

    /**
     * 获取临期药品统计列表
     */
    @GetMapping("/inventory/expiring")
    public BaseResponse<List<Map<String, Object>>> getExpiringMedicines(@RequestParam(required = false, defaultValue = "30") Integer days) {
        return ResultUtils.success(reportStatisticsService.getExpiringMedicines(days));
    }

    /**
     * 获取缺货药品统计
     */
    @GetMapping("/inventory/shortage")
    public BaseResponse<List<Map<String, Object>>> getShortageMedicines() {
        return ResultUtils.success(reportStatisticsService.getShortageMedicines());
    }

    /**
     * 获取按日销售统计
     */
    @GetMapping("/sales/daily")
    public BaseResponse<List<Map<String, Object>>> getDailySalesStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResultUtils.success(reportStatisticsService.getDailySalesStats(startDate, endDate));
    }

    /**
     * 获取按月销售统计
     */
    @GetMapping("/sales/monthly")
    public BaseResponse<List<Map<String, Object>>> getMonthlySalesStats(
            @RequestParam(required = false) String startMonth,
            @RequestParam(required = false) String endMonth) {
        return ResultUtils.success(reportStatisticsService.getMonthlySalesStats(startMonth, endMonth));
    }

    /**
     * 获取按年销售统计
     */
    @GetMapping("/sales/yearly")
    public BaseResponse<Map<String, Object>> getYearlySalesStats(@RequestParam(required = false) Integer year) {
        return ResultUtils.success(reportStatisticsService.getYearlySalesStats(year));
    }

    /**
     * 获取药品销售排行
     */
    @GetMapping("/sales/ranking")
    public BaseResponse<List<Map<String, Object>>> getSalesRanking(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "quantity") String sortBy) {
        return ResultUtils.success(reportStatisticsService.getSalesRanking(limit, sortBy));
    }

    /**
     * 获取首页仪表盘核心指标
     */
    @GetMapping("/dashboard/metrics")
    public BaseResponse<Map<String, Object>> getDashboardMetrics() {
        return ResultUtils.success(reportStatisticsService.getDashboardMetrics());
    }

    /**
     * 获取销售趋势数据（折线图）
     */
    @GetMapping("/chart/sales-trend")
    public BaseResponse<List<Map<String, Object>>> getSalesTrend(@RequestParam(required = false, defaultValue = "7") Integer days) {
        return ResultUtils.success(reportStatisticsService.getSalesTrend(days));
    }

    /**
     * 获取库存分类占比数据（饼图）
     */
    @GetMapping("/chart/category-pie")
    public BaseResponse<List<Map<String, Object>>> getCategoryInventoryPieData() {
        return ResultUtils.success(reportStatisticsService.getCategoryInventoryPieData());
    }

    /**
     * 获取出入库流水趋势
     */
    @GetMapping("/chart/inout-flow")
    public BaseResponse<List<Map<String, Object>>> getInOutFlowTrend(@RequestParam(required = false, defaultValue = "7") Integer days) {
        return ResultUtils.success(reportStatisticsService.getInOutFlowTrend(days));
    }

    /**
     * 导出统计报表为 Excel
     */
    @GetMapping("/export")
    public void exportReport(
            @RequestParam String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletResponse response) {
        List<Map<String, Object>> data = reportStatisticsService.exportReport(type, startDate, endDate);
        ExcelUtils.writeExcel(response, "统计报表", "统计报表", (Class<Map<String, Object>>) (Class<?>) Map.class, data);
    }
}
