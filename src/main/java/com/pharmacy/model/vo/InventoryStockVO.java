package com.pharmacy.model.vo;

import java.time.LocalDate;
/**
 * 库存信息视图对象
 */
public record InventoryStockVO(Long medicineId, String medicineName, String categoryName, String barcode,
        Integer totalStock, Integer safeStockMin, Integer safeStockMax, Integer batchCount, Integer nearExpiryCount,
        LocalDate nearestExpiryDate, String stockStatus) {
}
