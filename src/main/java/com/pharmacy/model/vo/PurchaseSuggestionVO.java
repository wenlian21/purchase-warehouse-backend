package com.pharmacy.model.vo;

import java.math.BigDecimal;

/**
 * 采购建议视图对象
 */
public record PurchaseSuggestionVO(
        Long medicineId,
        String medicineName,
        String specification,
        String manufacturer,
        Integer currentStock,
        Integer safeStockMin,
        Integer safeStockMax,
        Integer suggestedQuantity,
        BigDecimal referencePrice
) {
}
