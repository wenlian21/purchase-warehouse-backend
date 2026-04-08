package com.pharmacy.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 库存流水视图对象
 */
public record InventoryFlowVO(Long id, String medicineName, String batchNo, String bizType, String changeType,
        Integer quantity, BigDecimal unitPrice, String documentNo, String operatorName, String remark,
        LocalDateTime createTime) {
}
