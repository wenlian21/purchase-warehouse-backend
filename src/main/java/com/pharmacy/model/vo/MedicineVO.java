package com.pharmacy.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 药品视图对象
 */
public record MedicineVO(Long id, Long categoryId, String categoryName, String medicineName, String specification,
        String manufacturer, String barcode, String approvalNumber, String unitName, BigDecimal salePrice,
        Integer shelfLifeDays, Integer safeStockMin, Integer safeStockMax, Integer totalStock, Integer isPrescription,
        String status, LocalDateTime createTime) {
}
