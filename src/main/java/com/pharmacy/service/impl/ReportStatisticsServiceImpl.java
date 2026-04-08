package com.pharmacy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pharmacy.mapper.InventoryBatchMapper;
import com.pharmacy.mapper.InventoryFlowMapper;
import com.pharmacy.mapper.MedicineCategoryMapper;
import com.pharmacy.mapper.MedicineMapper;
import com.pharmacy.mapper.SalesOrderItemMapper;
import com.pharmacy.mapper.SalesOrderMapper;
import com.pharmacy.model.entity.InventoryBatch;
import com.pharmacy.model.entity.InventoryFlow;
import com.pharmacy.model.entity.Medicine;
import com.pharmacy.model.entity.MedicineCategory;
import com.pharmacy.model.entity.SalesOrder;
import com.pharmacy.model.entity.SalesOrderItem;
import com.pharmacy.service.ReportStatisticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 统计报表服务实现类
 */
@Service
public class ReportStatisticsServiceImpl implements ReportStatisticsService {

    private final MedicineMapper medicineMapper;
    private final MedicineCategoryMapper categoryMapper;
    private final InventoryBatchMapper inventoryBatchMapper;
    private final InventoryFlowMapper inventoryFlowMapper;
    private final SalesOrderMapper salesOrderMapper;
    private final SalesOrderItemMapper salesOrderItemMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public ReportStatisticsServiceImpl(MedicineMapper medicineMapper,
                                       MedicineCategoryMapper categoryMapper,
                                       InventoryBatchMapper inventoryBatchMapper,
                                       InventoryFlowMapper inventoryFlowMapper,
                                       SalesOrderMapper salesOrderMapper,
                                       SalesOrderItemMapper salesOrderItemMapper) {
        this.medicineMapper = medicineMapper;
        this.categoryMapper = categoryMapper;
        this.inventoryBatchMapper = inventoryBatchMapper;
        this.inventoryFlowMapper = inventoryFlowMapper;
        this.salesOrderMapper = salesOrderMapper;
        this.salesOrderItemMapper = salesOrderItemMapper;
    }

    /**
     * 获取库存总价值统计（按分类）
     *
     * @return 各类别库存价值和占比
     */
    @Override
    public List<Map<String, Object>> getInventoryValueByCategory() {
        List<Medicine> medicines = medicineMapper.selectList(new QueryWrapper<Medicine>().gt("totalStock", 0));

        List<Long> categoryIds = medicines.stream().map(Medicine::getCategoryId).distinct().collect(Collectors.toList());
        Map<Long, String> categoryMap = categoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(MedicineCategory::getId, MedicineCategory::getCategoryName));

        Map<Long, BigDecimal> categoryValueMap = new HashMap<>();
        for (Medicine medicine : medicines) {
            Long categoryId = medicine.getCategoryId();
            BigDecimal value = medicine.getSalePrice().multiply(BigDecimal.valueOf(medicine.getTotalStock()));
            categoryValueMap.put(categoryId, categoryValueMap.getOrDefault(categoryId, BigDecimal.ZERO).add(value));
        }

        BigDecimal totalValue = categoryValueMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : categoryValueMap.entrySet()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("categoryId", entry.getKey());
            item.put("categoryName", categoryMap.get(entry.getKey()));
            item.put("value", entry.getValue());
            item.put("percentage", totalValue.signum() > 0 ? entry.getValue().divide(totalValue, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
            result.add(item);
        }

        return result;
    }

    /**
     * 获取库存数量分布（按分类）
     *
     * @return 各类别库存数量分布
     */
    @Override
    public List<Map<String, Object>> getInventoryQuantityByCategory() {
        List<Medicine> medicines = medicineMapper.selectList(new QueryWrapper<Medicine>().gt("totalStock", 0));

        List<Long> categoryIds = medicines.stream().map(Medicine::getCategoryId).distinct().collect(Collectors.toList());
        Map<Long, String> categoryMap = categoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(MedicineCategory::getId, MedicineCategory::getCategoryName));

        Map<Long, Integer> categoryQuantityMap = new HashMap<>();
        for (Medicine medicine : medicines) {
            Long categoryId = medicine.getCategoryId();
            categoryQuantityMap.put(categoryId, categoryQuantityMap.getOrDefault(categoryId, 0) + medicine.getTotalStock());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : categoryQuantityMap.entrySet()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("categoryId", entry.getKey());
            item.put("categoryName", categoryMap.get(entry.getKey()));
            item.put("quantity", entry.getValue());
            result.add(item);
        }

        return result;
    }

    /**
     * 获取临期药品统计列表
     *
     * @param days 天数（默认 30 天）
     * @return 临期药品列表
     */
    @Override
    public List<Map<String, Object>> getExpiringMedicines(Integer days) {
        int expiryDays = days != null ? days : 30;
        LocalDate nearDate = LocalDate.now().plusDays(expiryDays);

        List<InventoryBatch> batches = inventoryBatchMapper.selectList(new QueryWrapper<InventoryBatch>()
                .gt("availableStock", 0));

        Map<Long, Medicine> medicineMap = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();

        for (InventoryBatch batch : batches) {
            if (batch.getExpiryDate().isBefore(LocalDate.now()) || batch.getExpiryDate().isAfter(nearDate)) {
                continue;
            }

            medicineMap.computeIfAbsent(batch.getMedicineId(), medicineMapper::selectById);
            Medicine medicine = medicineMap.get(batch.getMedicineId());
            if (medicine != null) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("medicineId", medicine.getId());
                item.put("medicineName", medicine.getMedicineName());
                item.put("specification", medicine.getSpecification());
                item.put("batchNo", batch.getBatchNo());
                item.put("expiryDate", batch.getExpiryDate());
                item.put("availableStock", batch.getAvailableStock());
                item.put("daysUntilExpiry", java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), batch.getExpiryDate()));
                result.add(item);
            }
        }

        return result.stream()
                .sorted((a, b) -> ((Long) a.get("daysUntilExpiry")).compareTo((Long) b.get("daysUntilExpiry")))
                .collect(Collectors.toList());
    }

    /**
     * 获取缺货药品统计
     *
     * @return 缺货药品列表
     */
    @Override
    public List<Map<String, Object>> getShortageMedicines() {
        List<Medicine> medicines = medicineMapper.selectList(new QueryWrapper<Medicine>()
                .le("totalStock", "safeStockMin"));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Medicine medicine : medicines) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("medicineId", medicine.getId());
            item.put("medicineName", medicine.getMedicineName());
            item.put("specification", medicine.getSpecification());
            item.put("currentStock", medicine.getTotalStock());
            item.put("safeStockMin", medicine.getSafeStockMin());
            item.put("shortageQuantity", medicine.getSafeStockMin() - medicine.getTotalStock());
            result.add(item);
        }

        return result;
    }

    /**
     * 获取按日销售统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日销售数据
     */
    @Override
    public List<Map<String, Object>> getDailySalesStats(String startDate, String endDate) {
        LocalDateTime startDateTime = parseDateTime(startDate, false);
        LocalDateTime endDateTime = parseDateTime(endDate, true);

        if (startDateTime == null) {
            startDateTime = LocalDateTime.of(LocalDate.now().minusDays(30), LocalTime.MIN);
        }
        if (endDateTime == null) {
            endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }

        List<SalesOrder> orders = salesOrderMapper.selectList(new QueryWrapper<SalesOrder>()
                .between("createTime", startDateTime, endDateTime)
                .eq("orderStatus", "已完成"));

        Map<LocalDate, List<SalesOrder>> orderMap = orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCreateTime().toLocalDate()));

        Map<Long, SalesOrder> orderInfoMap = orders.stream()
                .collect(Collectors.toMap(SalesOrder::getId, o -> o));

        List<SalesOrderItem> allItems = salesOrderItemMapper.selectList(new QueryWrapper<SalesOrderItem>()
                .in("sales_order_id", orderInfoMap.keySet()));

        Map<LocalDate, Integer> dailyQuantityMap = new HashMap<>();
        for (SalesOrderItem item : allItems) {
            SalesOrder order = orderInfoMap.get(item.getSalesOrderId());
            if (order != null) {
                LocalDate date = order.getCreateTime().toLocalDate();
                dailyQuantityMap.put(date, dailyQuantityMap.getOrDefault(date, 0) + item.getQuantity());
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate current = startDateTime.toLocalDate();
        while (!current.isAfter(endDateTime.toLocalDate())) {
            List<SalesOrder> dayOrders = orderMap.getOrDefault(current, List.of());
            BigDecimal amount = dayOrders.stream().map(SalesOrder::getActualAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            int quantity = dailyQuantityMap.getOrDefault(current, 0);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", current.format(DATE_FORMATTER));
            item.put("salesAmount", amount);
            item.put("salesQuantity", quantity);
            item.put("orderCount", dayOrders.size());
            result.add(item);

            current = current.plusDays(1);
        }

        return result;
    }

    /**
     * 获取按月销售统计
     *
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 每月销售数据
     */
    @Override
    public List<Map<String, Object>> getMonthlySalesStats(String startMonth, String endMonth) {
        YearMonth startYM = parseYearMonth(startMonth);
        YearMonth endYM = parseYearMonth(endMonth);

        if (startYM == null) {
            startYM = YearMonth.now().minusMonths(11);
        }
        if (endYM == null) {
            endYM = YearMonth.now();
        }

        List<Map<String, Object>> result = new ArrayList<>();
        YearMonth current = startYM;

        while (!current.isAfter(endYM)) {
            LocalDateTime startDateTime = LocalDateTime.of(current.atDay(1), LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(current.atEndOfMonth(), LocalTime.MAX);

            List<SalesOrder> orders = salesOrderMapper.selectList(new QueryWrapper<SalesOrder>()
                    .between("createTime", startDateTime, endDateTime)
                    .eq("orderStatus", "已完成"));

            BigDecimal amount = orders.stream().map(SalesOrder::getActualAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<Long, SalesOrder> orderInfoMap = orders.stream()
                    .collect(Collectors.toMap(SalesOrder::getId, o -> o));

            List<SalesOrderItem> monthItems = salesOrderItemMapper.selectList(new QueryWrapper<SalesOrderItem>()
                    .in("sales_order_id", orderInfoMap.keySet()));

            int quantity = monthItems.stream().mapToInt(SalesOrderItem::getQuantity).sum();

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("month", current.format(MONTH_FORMATTER));
            item.put("salesAmount", amount);
            item.put("salesQuantity", quantity);
            item.put("orderCount", orders.size());
            result.add(item);

            current = current.plusMonths(1);
        }

        return result;
    }

    /**
     * 获取按年销售统计
     *
     * @param year 年份
     * @return 年度销售数据
     */
    @Override
    public Map<String, Object> getYearlySalesStats(Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        LocalDateTime startDateTime = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        List<SalesOrder> currentYearOrders = salesOrderMapper.selectList(new QueryWrapper<SalesOrder>()
                .between("createTime", startDateTime, endDateTime)
                .eq("orderStatus", "已完成"));

        BigDecimal currentAmount = currentYearOrders.stream().map(SalesOrder::getActualAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Long, SalesOrder> orderInfoMap = currentYearOrders.stream()
                .collect(Collectors.toMap(SalesOrder::getId, o -> o));

        List<SalesOrderItem> yearItems = salesOrderItemMapper.selectList(new QueryWrapper<SalesOrderItem>()
                .in("sales_order_id", orderInfoMap.keySet()));

        int currentQuantity = yearItems.stream().mapToInt(SalesOrderItem::getQuantity).sum();

        LocalDateTime prevStartDateTime = LocalDateTime.of(year - 1, 1, 1, 0, 0, 0);
        LocalDateTime prevEndDateTime = LocalDateTime.of(year - 1, 12, 31, 23, 59, 59);

        List<SalesOrder> prevYearOrders = salesOrderMapper.selectList(new QueryWrapper<SalesOrder>()
                .between("createTime", prevStartDateTime, prevEndDateTime)
                .eq("orderStatus", "已完成"));

        BigDecimal prevAmount = prevYearOrders.stream().map(SalesOrder::getActualAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal growthRate = prevAmount.signum() > 0
                ? currentAmount.subtract(prevAmount).divide(prevAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("year", year);
        result.put("totalAmount", currentAmount);
        result.put("totalQuantity", currentQuantity);
        result.put("orderCount", currentYearOrders.size());
        result.put("growthRate", growthRate);
        result.put("prevYearAmount", prevAmount);

        return result;
    }

    /**
     * 获取药品销售排行
     *
     * @param limit 返回数量限制
     * @param sortBy 排序字段（quantity-销售量，amount-销售额）
     * @return 销售排行列表
     */
    @Override
    public List<Map<String, Object>> getSalesRanking(Integer limit, String sortBy) {
        int rankLimit = limit != null ? limit : 10;
        String sortField = StringUtils.defaultIfBlank(sortBy, "quantity");

        List<SalesOrderItem> items = salesOrderItemMapper.selectList(new QueryWrapper<SalesOrderItem>());

        Map<Long, Map<String, Object>> medicineStats = new HashMap<>();
        for (SalesOrderItem item : items) {
            Long medicineId = item.getMedicineId();
            medicineStats.computeIfAbsent(medicineId, k -> {
                Map<String, Object> stats = new LinkedHashMap<>();
                stats.put("medicineId", medicineId);
                stats.put("medicineName", item.getMedicineName());
                stats.put("totalQuantity", 0);
                stats.put("totalAmount", BigDecimal.ZERO);
                return stats;
            });

            Map<String, Object> stats = medicineStats.get(medicineId);
            stats.put("totalQuantity", (Integer) stats.get("totalQuantity") + item.getQuantity());
            stats.put("totalAmount", ((BigDecimal) stats.get("totalAmount")).add(item.getLineAmount()));
        }

        List<Map<String, Object>> result = new ArrayList<>(medicineStats.values());
        result.sort((a, b) -> {
            if ("amount".equals(sortField)) {
                return ((BigDecimal) b.get("totalAmount")).compareTo((BigDecimal) a.get("totalAmount"));
            } else {
                return ((Integer) b.get("totalQuantity")).compareTo((Integer) a.get("totalQuantity"));
            }
        });

        return result.stream().limit(rankLimit).collect(Collectors.toList());
    }

    /**
     * 获取首页仪表盘核心指标
     *
     * @return 仪表盘数据
     */
    @Override
    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<SalesOrder> todayOrders = salesOrderMapper.selectList(new QueryWrapper<SalesOrder>()
                .between("createTime", todayStart, todayEnd)
                .eq("orderStatus", "已完成"));

        BigDecimal todaySales = todayOrders.stream().map(SalesOrder::getActualAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        metrics.put("todaySalesAmount", todaySales);
        metrics.put("todayOrderCount", todayOrders.size());

        long lowStockCount = medicineMapper.selectCount(new QueryWrapper<Medicine>()
                .le("totalStock", "safeStockMin"));
        metrics.put("lowStockCount", lowStockCount);

        LocalDate nearDate = LocalDate.now().plusDays(30);
        long expiringCount = inventoryBatchMapper.selectList(new QueryWrapper<InventoryBatch>().gt("availableStock", 0))
                .stream().filter(batch -> !batch.getExpiryDate().isBefore(LocalDate.now()) && !batch.getExpiryDate().isAfter(nearDate))
                .count();
        metrics.put("expiringSoonCount", expiringCount);

        List<Medicine> allMedicines = medicineMapper.selectList(new QueryWrapper<Medicine>().gt("totalStock", 0));
        BigDecimal totalInventoryValue = allMedicines.stream()
                .map(m -> m.getSalePrice().multiply(BigDecimal.valueOf(m.getTotalStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        metrics.put("totalInventoryValue", totalInventoryValue);

        return metrics;
    }

    /**
     * 获取销售趋势数据（折线图）
     *
     * @param days 天数（7 或 30）
     * @return 销售趋势数据
     */
    @Override
    public List<Map<String, Object>> getSalesTrend(Integer days) {
        int trendDays = days != null ? days : 7;
        LocalDate startDate = LocalDate.now().minusDays(trendDays - 1);

        List<SalesOrder> orders = salesOrderMapper.selectList(new QueryWrapper<SalesOrder>()
                .ge("createTime", LocalDateTime.of(startDate, LocalTime.MIN))
                .eq("orderStatus", "已完成")
                .orderByAsc("createTime"));

        Map<LocalDate, List<SalesOrder>> orderMap = orders.stream()
                .collect(Collectors.groupingBy(o -> o.getCreateTime().toLocalDate()));

        Map<Long, SalesOrder> orderInfoMap = orders.stream()
                .collect(Collectors.toMap(SalesOrder::getId, o -> o));

        List<SalesOrderItem> allItems = new ArrayList<>();
        if (!orderInfoMap.isEmpty()) {
            allItems = salesOrderItemMapper.selectList(new QueryWrapper<SalesOrderItem>()
                    .in("salesOrderId", orderInfoMap.keySet()));
        }
        Map<LocalDate, Integer> dailyQuantityMap = new HashMap<>();
        for (SalesOrderItem item : allItems) {
            SalesOrder order = orderInfoMap.get(item.getSalesOrderId());
            if (order != null) {
                LocalDate date = order.getCreateTime().toLocalDate();
                dailyQuantityMap.put(date, dailyQuantityMap.getOrDefault(date, 0) + item.getQuantity());
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < trendDays; i++) {
            LocalDate current = startDate.plusDays(i);
            List<SalesOrder> dayOrders = orderMap.getOrDefault(current, List.of());
            BigDecimal amount = dayOrders.stream().map(SalesOrder::getActualAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            int quantity = dailyQuantityMap.getOrDefault(current, 0);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", current.format(DATE_FORMATTER));
            item.put("salesAmount", amount);
            item.put("salesQuantity", quantity);
            result.add(item);
        }

        return result;
    }


    /**
     * 获取库存分类占比数据（饼图）
     *
     * @return 库存分类占比
     */
    @Override
    public List<Map<String, Object>> getCategoryInventoryPieData() {
        return getInventoryValueByCategory();
    }

    /**
     * 获取出入库流水趋势
     *
     * @param days 天数
     * @return 出入库趋势数据
     */
    @Override
    public List<Map<String, Object>> getInOutFlowTrend(Integer days) {
        int flowDays = days != null ? days : 7;
        LocalDate startDate = LocalDate.now().minusDays(flowDays - 1);

        List<InventoryFlow> flows = inventoryFlowMapper.selectList(new QueryWrapper<InventoryFlow>()
                .ge("createTime", LocalDateTime.of(startDate, LocalTime.MIN)));

        Map<LocalDate, Map<String, Integer>> dailyFlowMap = new HashMap<>();
        for (InventoryFlow flow : flows) {
            LocalDate date = flow.getCreateTime().toLocalDate();
            dailyFlowMap.computeIfAbsent(date, k -> {
                Map<String, Integer> stats = new HashMap<>();
                stats.put("inQuantity", 0);
                stats.put("outQuantity", 0);
                return stats;
            });

            Map<String, Integer> stats = dailyFlowMap.get(date);
            if ("入库".equals(flow.getChangeType())) {
                stats.put("inQuantity", stats.get("inQuantity") + flow.getQuantity());
            } else if ("出库".equals(flow.getChangeType())) {
                stats.put("outQuantity", stats.get("outQuantity") + flow.getQuantity());
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < flowDays; i++) {
            LocalDate current = startDate.plusDays(i);
            Map<String, Integer> stats = dailyFlowMap.getOrDefault(current, Map.of("inQuantity", 0, "outQuantity", 0));

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", current.format(DATE_FORMATTER));
            item.put("inQuantity", stats.get("inQuantity"));
            item.put("outQuantity", stats.get("outQuantity"));
            result.add(item);
        }

        return result;
    }

    /**
     * 导出统计报表为 Excel
     *
     * @param type 报表类型（inventory-库存，sales-销售）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel 数据列表
     */
    @Override
    public List<Map<String, Object>> exportReport(String type, String startDate, String endDate) {
        if ("inventory".equals(type)) {
            List<Map<String, Object>> result = new ArrayList<>();

            List<Map<String, Object>> inventoryValue = getInventoryValueByCategory();
            for (Map<String, Object> item : inventoryValue) {
                Map<String, Object> row = new LinkedHashMap<>(item);
                row.put("reportType", "库存价值统计");
                result.add(row);
            }

            List<Map<String, Object>> shortage = getShortageMedicines();
            for (Map<String, Object> item : shortage) {
                Map<String, Object> row = new LinkedHashMap<>(item);
                row.put("reportType", "缺货药品统计");
                result.add(row);
            }

            List<Map<String, Object>> expiring = getExpiringMedicines(30);
            for (Map<String, Object> item : expiring) {
                Map<String, Object> row = new LinkedHashMap<>(item);
                row.put("reportType", "临期药品统计");
                result.add(row);
            }

            return result;
        } else if ("sales".equals(type)) {
            return getDailySalesStats(startDate, endDate);
        }

        return List.of();
    }

    /**
     * 解析日期时间
     */
    private LocalDateTime parseDateTime(String value, boolean endOfDay) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception ignored) {
        }
        try {
            LocalDate date = LocalDate.parse(value, DATE_FORMATTER);
            return endOfDay ? LocalDateTime.of(date, LocalTime.MAX) : LocalDateTime.of(date, LocalTime.MIN);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 解析年月
     */
    private YearMonth parseYearMonth(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return YearMonth.parse(value, MONTH_FORMATTER);
        } catch (Exception ignored) {
        }
        return null;
    }
}
