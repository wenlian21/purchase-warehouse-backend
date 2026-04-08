package com.pharmacy.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购订单视图对象
 */
public record PurchaseOrderVO(
        Long id,
        String orderNo,
        Long supplierId,
        String supplierName,
        BigDecimal totalAmount,
        Integer totalCount,
        String orderStatus,
        String auditOpinion,
        Long auditorId,
        String auditorName,
        LocalDateTime auditTime,
        LocalDateTime createTime,
        List<PurchaseOrderItemVO> items
) {
}
