package com.pharmacy.model.vo;
/**
 * 库存预警信息
 */
public record InventoryAlertVO(String alertType, Long medicineId, String medicineName, String content,
        String levelTag) {
}
