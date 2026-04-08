package com.pharmacy.model.dto.inventory;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存查询请求 DTO
 * 用于封装库存查询条件，支持分页
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryQueryRequest extends PageRequest {

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 分类 ID
     */
    private Long categoryId;
}
