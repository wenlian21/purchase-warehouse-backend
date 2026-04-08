package com.pharmacy.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 订单信息
 */
public record SalesOrderVO(Long id, String orderNo, String memberName, String memberPhone, BigDecimal actualAmount,
        BigDecimal discountAmount, Integer pointsEarned, Integer prescriptionChecked, String cashierName,
        String orderStatus, String itemSummary, LocalDateTime createTime) {
}
