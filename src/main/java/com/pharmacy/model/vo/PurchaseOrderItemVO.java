package com.pharmacy.model.vo;

import java.math.BigDecimal;

/**
 * 采购订单项视图对象
 */
public record PurchaseOrderItemVO(
        Long id,
        Long medicineId,
        String medicineName,
        Integer quantity,
        BigDecimal purchasePrice,
        BigDecimal lineAmount
) {
}
