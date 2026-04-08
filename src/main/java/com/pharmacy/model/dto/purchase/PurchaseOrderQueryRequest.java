package com.pharmacy.model.dto.purchase;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 采购订单查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderQueryRequest extends PageRequest implements Serializable {

    /**
     * 采购单号
     */
    private String orderNo;

    /**
     * 药品名称关键词
     */
    private String keyword;

    /**
     * 供应商 ID
     */
    private Long supplierId;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    private static final long serialVersionUID = 1L;
}
