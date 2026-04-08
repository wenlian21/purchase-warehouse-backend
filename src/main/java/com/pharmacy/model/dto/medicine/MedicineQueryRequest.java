package com.pharmacy.model.dto.medicine;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 药品查询请求 DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MedicineQueryRequest extends PageRequest {

    /**
     * 搜索关键字（支持药品名称、条形码）
     */
    private String keyword;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 是否处方药（0-否，1-是）
     */
    private Integer isPrescription;

    /**
     * 状态
     */
    private String status;
}
