package com.pharmacy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pharmacy.common.ErrorCode;
import com.pharmacy.exception.ThrowUtils;
import com.pharmacy.mapper.InventoryBatchMapper;
import com.pharmacy.mapper.MedicineMapper;
import com.pharmacy.mapper.PurchaseOrderItemMapper;
import com.pharmacy.mapper.PurchaseOrderMapper;
import com.pharmacy.mapper.SupplierMapper;
import com.pharmacy.model.dto.purchase.PurchaseOrderAuditRequest;
import com.pharmacy.model.dto.purchase.PurchaseOrderCreateRequest;
import com.pharmacy.model.dto.purchase.PurchaseOrderQueryRequest;
import com.pharmacy.model.entity.InventoryBatch;
import com.pharmacy.model.entity.Medicine;
import com.pharmacy.model.entity.PurchaseOrder;
import com.pharmacy.model.entity.PurchaseOrderItem;
import com.pharmacy.model.entity.Supplier;
import com.pharmacy.model.excel.PurchaseOrderExportRow;
import com.pharmacy.model.vo.PurchaseOrderItemVO;
import com.pharmacy.model.vo.PurchaseOrderVO;
import com.pharmacy.model.vo.PurchaseSuggestionVO;
import com.pharmacy.service.InventoryService;
import com.pharmacy.service.PurchaseOrderService;
import com.pharmacy.utils.ExcelUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购订单服务实现类
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Resource
    private PurchaseOrderMapper purchaseOrderMapper;

    @Resource
    private PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Resource
    private MedicineMapper medicineMapper;

    @Resource
    private SupplierMapper supplierMapper;

    @Resource
    private InventoryBatchMapper inventoryBatchMapper;

    @Resource
    private InventoryService inventoryService;

    /**
     * 创建采购订单
     *
     * @param request 创建请求
     * @return 订单 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(PurchaseOrderCreateRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        ThrowUtils.throwIf(request.getSupplierId() == null, ErrorCode.PARAMS_ERROR, "请选择供应商");
        ThrowUtils.throwIf(request.getItems() == null || request.getItems().isEmpty(), ErrorCode.PARAMS_ERROR, "请添加采购商品");

        Supplier supplier = supplierMapper.selectById(request.getSupplierId());
        ThrowUtils.throwIf(supplier == null, ErrorCode.NOT_FOUND_ERROR, "供应商不存在");
        ThrowUtils.throwIf(Objects.equals(supplier.getIsBlacklisted(), 1), ErrorCode.OPERATION_ERROR, "黑名单供应商不能创建采购单");

        PurchaseOrder order = new PurchaseOrder();
        String orderNo = "PO" + System.currentTimeMillis();
        order.setOrderNo(orderNo);
        order.setSupplierId(request.getSupplierId());
        order.setOrderStatus("待审核");
        order.setTotalCount(0);
        order.setTotalAmount(BigDecimal.ZERO);

        purchaseOrderMapper.insert(order);

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalCount = 0;

        for (PurchaseOrderCreateRequest.PurchaseOrderItemRequest itemRequest : request.getItems()) {
            ThrowUtils.throwIf(itemRequest.getMedicineId() == null, ErrorCode.PARAMS_ERROR, "药品 ID 不能为空");
            ThrowUtils.throwIf(itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0, ErrorCode.PARAMS_ERROR, "采购数量必须大于 0");
            ThrowUtils.throwIf(itemRequest.getPurchasePrice() == null || itemRequest.getPurchasePrice().signum() < 0, ErrorCode.PARAMS_ERROR, "采购单价不合法");

            Medicine medicine = medicineMapper.selectById(itemRequest.getMedicineId());
            ThrowUtils.throwIf(medicine == null, ErrorCode.NOT_FOUND_ERROR, "药品不存在");

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrderId(order.getId());
            item.setMedicineId(itemRequest.getMedicineId());
            item.setMedicineName(medicine.getMedicineName());
            item.setQuantity(itemRequest.getQuantity());
            item.setPurchasePrice(itemRequest.getPurchasePrice());
            item.setLineAmount(itemRequest.getPurchasePrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

            purchaseOrderItemMapper.insert(item);

            totalAmount = totalAmount.add(item.getLineAmount());
            totalCount += itemRequest.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        order.setTotalCount(totalCount);
        purchaseOrderMapper.updateById(order);

        return order.getId();
    }

    /**
     * 分页查询采购订单列表
     *
     * @param request 查询请求
     * @return 分页数据
     */
    @Override
    public Page<PurchaseOrderVO> listOrders(PurchaseOrderQueryRequest request) {
        long current = request == null ? 1 : request.getCurrent();
        long pageSize = request == null ? 10 : request.getPageSize();

        Page<PurchaseOrder> orderPage = new Page<>(current, pageSize);
        QueryWrapper<PurchaseOrder> queryWrapper = buildQueryWrapper(request);

        Page<PurchaseOrder> pageResult = purchaseOrderMapper.selectPage(orderPage, queryWrapper);

        List<Long> orderIds = pageResult.getRecords().stream().map(PurchaseOrder::getId).collect(Collectors.toList());
        List<Long> supplierIds = pageResult.getRecords().stream().map(PurchaseOrder::getSupplierId).distinct().collect(Collectors.toList());

        Map<Long, String> supplierMap = supplierIds.isEmpty() ? Map.of()
                : supplierMapper.selectBatchIds(supplierIds).stream()
                .collect(Collectors.toMap(Supplier::getId, Supplier::getSupplierName));

        Map<Long, List<PurchaseOrderItem>> itemMap = orderIds.isEmpty() ? Map.of()
                : purchaseOrderItemMapper.selectList(new QueryWrapper<PurchaseOrderItem>().in("purchaseOrderId", orderIds))
                .stream().collect(Collectors.groupingBy(PurchaseOrderItem::getPurchaseOrderId));

        List<PurchaseOrderVO> voList = pageResult.getRecords().stream().map(order -> {
            List<PurchaseOrderItemVO> itemVOS = itemMap.getOrDefault(order.getId(), List.of()).stream()
                    .map(item -> new PurchaseOrderItemVO(item.getId(), item.getMedicineId(), item.getMedicineName(),
                            item.getQuantity(), item.getPurchasePrice(), item.getLineAmount()))
                    .collect(Collectors.toList());

            return new PurchaseOrderVO(
                    order.getId(),
                    order.getOrderNo(),
                    order.getSupplierId(),
                    supplierMap.get(order.getSupplierId()),
                    order.getTotalAmount(),
                    order.getTotalCount(),
                    order.getOrderStatus(),
                    order.getAuditOpinion(),
                    order.getAuditorId(),
                    order.getAuditorName(),
                    order.getAuditTime(),
                    order.getCreateTime(),
                    itemVOS
            );
        }).collect(Collectors.toList());

        Page<PurchaseOrderVO> result = new Page<>(current, pageSize, pageResult.getTotal());
        result.setRecords(voList);
        return result;
    }

    /**
     * 获取采购订单详情
     *
     * @param orderId 订单 ID
     * @return 订单详情
     */
    @Override
    public PurchaseOrderVO getOrderDetail(Long orderId) {
        ThrowUtils.throwIf(orderId == null, ErrorCode.PARAMS_ERROR, "订单 ID 不能为空");
        PurchaseOrder order = purchaseOrderMapper.selectById(orderId);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR, "订单不存在");

        Supplier supplier = supplierMapper.selectById(order.getSupplierId());
        List<PurchaseOrderItem> items = purchaseOrderItemMapper.selectList(new QueryWrapper<PurchaseOrderItem>()
                .eq("purchaseOrderId", orderId));

        List<PurchaseOrderItemVO> itemVOS = items.stream()
                .map(item -> new PurchaseOrderItemVO(item.getId(), item.getMedicineId(), item.getMedicineName(),
                        item.getQuantity(), item.getPurchasePrice(), item.getLineAmount()))
                .collect(Collectors.toList());

        return new PurchaseOrderVO(
                order.getId(),
                order.getOrderNo(),
                order.getSupplierId(),
                supplier != null ? supplier.getSupplierName() : null,
                order.getTotalAmount(),
                order.getTotalCount(),
                order.getOrderStatus(),
                order.getAuditOpinion(),
                order.getAuditorId(),
                order.getAuditorName(),
                order.getAuditTime(),
                order.getCreateTime(),
                itemVOS
        );
    }

    /**
     * 审核采购订单
     *
     * @param request 审核请求
     * @return 是否成功
     */
    @Override
    public boolean auditOrder(PurchaseOrderAuditRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        ThrowUtils.throwIf(request.getOrderId() == null, ErrorCode.PARAMS_ERROR, "订单 ID 不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(request.getAuditResult()), ErrorCode.PARAMS_ERROR, "审核结果不能为空");

        PurchaseOrder order = purchaseOrderMapper.selectById(request.getOrderId());
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        ThrowUtils.throwIf(!"待审核".equals(order.getOrderStatus()), ErrorCode.OPERATION_ERROR, "订单状态不允许审核");

        if ("pass".equals(request.getAuditResult())) {
            order.setOrderStatus("审核通过");
        } else if ("reject".equals(request.getAuditResult())) {
            order.setOrderStatus("审核不通过");
            order.setAuditOpinion(request.getAuditOpinion());
        } else {
            ThrowUtils.throwIf(true, ErrorCode.PARAMS_ERROR, "审核结果不合法");
        }

        order.setAuditorName("当前用户");
        order.setAuditTime(LocalDateTime.now());
        purchaseOrderMapper.updateById(order);

        return true;
    }

    /**
     * 删除采购订单
     *
     * @param orderId 订单 ID
     * @return 是否成功
     */
    @Override
    public boolean deleteOrder(Long orderId) {
        ThrowUtils.throwIf(orderId == null, ErrorCode.PARAMS_ERROR, "订单 ID 不能为空");
        PurchaseOrder order = purchaseOrderMapper.selectById(orderId);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        ThrowUtils.throwIf(!"待审核".equals(order.getOrderStatus()), ErrorCode.OPERATION_ERROR, "只有待审核状态的订单可以删除");

        purchaseOrderMapper.deleteById(orderId);
        purchaseOrderItemMapper.delete(new QueryWrapper<PurchaseOrderItem>().eq("purchaseOrderId", orderId));

        return true;
    }

    /**
     * 生成智能补货建议列表
     *
     * @return 补货建议列表
     */
    @Override
    public List<PurchaseSuggestionVO> generatePurchaseSuggestions() {
        List<Medicine> lowStockMedicines = medicineMapper.selectList(new QueryWrapper<Medicine>()
                .apply("totalStock <= safe_stock_min"));

        return lowStockMedicines.stream().map(medicine -> {
            int suggestedQuantity = medicine.getSafeStockMax() - medicine.getTotalStock();
            if (suggestedQuantity <= 0) {
                suggestedQuantity = medicine.getSafeStockMin() * 2 - medicine.getTotalStock();
            }

            return new PurchaseSuggestionVO(
                    medicine.getId(),
                    medicine.getMedicineName(),
                    medicine.getSpecification(),
                    medicine.getManufacturer(),
                    medicine.getTotalStock(),
                    medicine.getSafeStockMin(),
                    medicine.getSafeStockMax(),
                    suggestedQuantity,
                    medicine.getSalePrice().multiply(BigDecimal.valueOf(0.7))
            );
        }).collect(Collectors.toList());
    }

    /**
     * 一键生成采购单（基于智能补货建议）
     *
     * @param supplierId 供应商 ID
     * @param medicineIds 药品 ID 列表（可选，为空则全部添加）
     * @return 订单 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long generatePurchaseOrderFromSuggestions(Long supplierId, List<Long> medicineIds) {
        ThrowUtils.throwIf(supplierId == null, ErrorCode.PARAMS_ERROR, "请选择供应商");

        List<PurchaseSuggestionVO> suggestions = generatePurchaseSuggestions();
        if (medicineIds != null && !medicineIds.isEmpty()) {
            suggestions = suggestions.stream()
                    .filter(s -> medicineIds.contains(s.medicineId()))
                    .collect(Collectors.toList());
        }

        ThrowUtils.throwIf(suggestions.isEmpty(), ErrorCode.OPERATION_ERROR, "没有需要补货的药品");

        PurchaseOrderCreateRequest request = new PurchaseOrderCreateRequest();
        request.setSupplierId(supplierId);

        List<PurchaseOrderCreateRequest.PurchaseOrderItemRequest> items = new ArrayList<>();
        for (PurchaseSuggestionVO suggestion : suggestions) {
            PurchaseOrderCreateRequest.PurchaseOrderItemRequest item = new PurchaseOrderCreateRequest.PurchaseOrderItemRequest();
            item.setMedicineId(suggestion.medicineId());
            item.setQuantity(suggestion.suggestedQuantity());
            item.setPurchasePrice(suggestion.referencePrice());
            items.add(item);
        }

        request.setItems(items);
        return createOrder(request);
    }

    /**
     * 采购入库确认
     *
     * @param orderId 采购订单 ID
     * @param operatorName 操作人姓名
     * @param remark 备注
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmPurchaseInbound(Long orderId, String operatorName, String remark) {
        ThrowUtils.throwIf(orderId == null, ErrorCode.PARAMS_ERROR, "订单 ID 不能为空");
        PurchaseOrder order = purchaseOrderMapper.selectById(orderId);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        ThrowUtils.throwIf(!"审核通过".equals(order.getOrderStatus()), ErrorCode.OPERATION_ERROR, "只有审核通过的订单可以入库");

        List<PurchaseOrderItem> items = purchaseOrderItemMapper.selectList(new QueryWrapper<PurchaseOrderItem>()
                .eq("purchaseOrderId", orderId));
        ThrowUtils.throwIf(items.isEmpty(), ErrorCode.OPERATION_ERROR, "订单没有可入库的商品");

        for (PurchaseOrderItem item : items) {
            Medicine medicine = medicineMapper.selectById(item.getMedicineId());
            ThrowUtils.throwIf(medicine == null, ErrorCode.NOT_FOUND_ERROR, "药品不存在：" + item.getMedicineName());

            String batchNo = "B" + System.currentTimeMillis() + "-" + item.getMedicineId();
            LocalDate productionDate = LocalDate.now();
            LocalDate expiryDate = productionDate.plusDays(medicine.getShelfLifeDays());

            com.pharmacy.model.dto.inventory.InventoryInRequest inRequest = new com.pharmacy.model.dto.inventory.InventoryInRequest();
            inRequest.setMedicineId(item.getMedicineId());
            inRequest.setSupplierId(order.getSupplierId());
            inRequest.setBatchNo(batchNo);
            inRequest.setQuantity(item.getQuantity());
            inRequest.setPurchasePrice(item.getPurchasePrice());
            inRequest.setProductionDate(productionDate);
            inRequest.setExpiryDate(expiryDate);
            inRequest.setStorageLocation("默认库位");
            inRequest.setOperatorName(operatorName);
            inRequest.setRemark(remark != null ? remark : "采购入库：" + order.getOrderNo());
            inRequest.setBizType("采购入库");

            inventoryService.stockIn(inRequest);
        }

        order.setOrderStatus("已完成");
        purchaseOrderMapper.updateById(order);

        return true;
    }

    /**
     * 导出采购订单为 Excel
     *
     * @param request 查询请求
     * @return Excel 数据
     */
    @Override
    public List<PurchaseOrderExportRow> exportOrders(PurchaseOrderQueryRequest request) {
        Page<PurchaseOrderVO> page = listOrders(request);
        List<PurchaseOrderVO> orders = page.getRecords();

        List<Long> medicineIds = new ArrayList<>();
        for (PurchaseOrderVO order : orders) {
            for (PurchaseOrderItemVO item : order.items()) {
                medicineIds.add(item.medicineId());
            }
        }

        Map<Long, Medicine> medicineMap = medicineIds.isEmpty() ? Map.of()
                : medicineMapper.selectBatchIds(medicineIds).stream()
                .collect(Collectors.toMap(Medicine::getId, m -> m));

        List<PurchaseOrderExportRow> rows = new ArrayList<>();
        for (PurchaseOrderVO order : orders) {
            for (PurchaseOrderItemVO item : order.items()) {
                Medicine medicine = medicineMap.get(item.medicineId());
                rows.add(new PurchaseOrderExportRow(
                        order.orderNo(),
                        order.supplierName(),
                        item.medicineName(),
                        medicine != null ? medicine.getSpecification() : "-",
                        item.quantity(),
                        item.purchasePrice(),
                        item.lineAmount(),
                        order.orderStatus(),
                        order.createTime()
                ));
            }
        }
        return rows;
    }

    /**
     * 构建查询条件
     *
     * @param request 查询请求
     * @return 查询条件
     */
    private QueryWrapper<PurchaseOrder> buildQueryWrapper(PurchaseOrderQueryRequest request) {
        QueryWrapper<PurchaseOrder> queryWrapper = new QueryWrapper<>();

        if (request != null) {
            queryWrapper.eq(StringUtils.isNotBlank(request.getOrderNo()), "orderNo", request.getOrderNo());
            queryWrapper.eq(request.getSupplierId() != null, "supplierId", request.getSupplierId());
            queryWrapper.eq(StringUtils.isNotBlank(request.getOrderStatus()), "orderStatus", request.getOrderStatus());

            LocalDateTime startTime = parseDateTime(request.getStartTime(), false);
            LocalDateTime endTime = parseDateTime(request.getEndTime(), true);
            queryWrapper.ge(startTime != null, "createTime", startTime);
            queryWrapper.le(endTime != null, "createTime", endTime);

            if (StringUtils.isNotBlank(request.getKeyword())) {
                Set<Long> medicineIds = medicineMapper.selectList(new QueryWrapper<Medicine>()
                                .like("medicineName", request.getKeyword()))
                        .stream().map(Medicine::getId).collect(Collectors.toSet());

                if (!medicineIds.isEmpty()) {
                    List<Long> orderIds = purchaseOrderItemMapper.selectList(new QueryWrapper<PurchaseOrderItem>()
                                    .in("medicineId", medicineIds))
                            .stream().map(PurchaseOrderItem::getPurchaseOrderId).distinct().collect(Collectors.toList());
                    queryWrapper.in(!orderIds.isEmpty(), "id", orderIds);
                } else {
                    queryWrapper.and(wrapper -> wrapper.like("orderNo", request.getKeyword()));
                }
            }
        }

        return queryWrapper.orderByDesc("createTime");
    }

    /**
     * 解析日期时间字符串
     *
     * @param value    日期或日期时间字符串
     * @param endOfDay 是否返回当天结束时间
     * @return 解析后的 LocalDateTime，失败返回 null
     */
    private LocalDateTime parseDateTime(String value, boolean endOfDay) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
        } catch (Exception ignored) {
        }
        try {
            LocalDate date = LocalDate.parse(value, DATE_FORMATTER);
            return endOfDay ? LocalDateTime.of(date, LocalTime.MAX) : LocalDateTime.of(date, LocalTime.MIN);
        } catch (Exception ignored) {
        }
        return null;
    }
}
