package com.pharmacy.model.dto.inventory;

import lombok.Data;
/**
 * 出库请求 DTO
 * 用于封装药品出库操作的相关参数
 */
@Data
public class InventoryOutRequest {
    /**
     * 药品 ID
     */
    private Long medicineId;

    /**
     * 出库数量
     */
    private Integer quantity;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 操作员姓名
     */
    private String operatorName;

    /**
     * 单据编号
     */
    private String documentNo;

    /**
     * 备注信息
     */
    private String remark;
}
