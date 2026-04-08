package com.pharmacy.model.dto.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * 入库请求 DTO
 * 用于封装药品入库操作的相关参数
 */
@Data
public class InventoryInRequest {

    /**
     * 药品 ID
     */
    private Long medicineId;

    /**
     * 供应商 ID
     */
    private Long supplierId;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 有效期至
     */
    private LocalDate expiryDate;

    /**
     * 存储位置
     */
    private String storageLocation;

    /**
     * 入库数量
     */
    private Integer quantity;

    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 操作员姓名
     */
    private String operatorName;

    /**
     * 备注信息
     */
    private String remark;
}
