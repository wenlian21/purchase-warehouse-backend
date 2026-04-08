package com.pharmacy.model.dto.medicine;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 药品保存请求 DTO
 */
@Data
public class MedicineSaveRequest {

    /**
     * 药品 ID（新增时为空，更新时必填）
     */
    private Long id;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 药品名称
     */
    private String medicineName;

    /**
     * 规格
     */
    private String specification;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 批准文号
     */
    private String approvalNumber;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 销售价格
     */
    private BigDecimal salePrice;

    /**
     * 有效期天数
     */
    private Integer shelfLifeDays;

    /**
     * 安全库存最小值
     */
    private Integer safeStockMin;

    /**
     * 安全库存最大值
     */
    private Integer safeStockMax;

    /**
     * 是否处方药（0-否，1-是）
     */
    private Integer isPrescription;

    /**
     * 状态
     */
    private String status;
}
