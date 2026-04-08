package com.pharmacy.model.dto.purchase;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 采购订单创建请求
 */
@Data
public class PurchaseOrderCreateRequest implements Serializable {

    /**
     * 供应商 ID
     */
    private Long supplierId;

    /**
     * 采购明细列表
     */
    private List<PurchaseOrderItemRequest> items;

    /**
     * 备注
     */
    private String remark;

    @Data
    public static class PurchaseOrderItemRequest implements Serializable {
        /**
         * 药品 ID
         */
        private Long medicineId;

        /**
         * 采购数量
         */
        private Integer quantity;

        /**
         * 采购单价
         */
        private java.math.BigDecimal purchasePrice;
    }

    private static final long serialVersionUID = 1L;
}
