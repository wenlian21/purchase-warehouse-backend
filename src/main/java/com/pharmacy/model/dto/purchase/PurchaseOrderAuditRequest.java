package com.pharmacy.model.dto.purchase;

import java.io.Serializable;
import lombok.Data;

/**
 * 采购订单审核请求
 */
@Data
public class PurchaseOrderAuditRequest implements Serializable {

    /**
     * 采购订单 ID
     */
    private Long orderId;

    /**
     * 审核结果（pass-通过，reject-拒绝）
     */
    private String auditResult;

    /**
     * 审核意见
     */
    private String auditOpinion;

    private static final long serialVersionUID = 1L;
}
