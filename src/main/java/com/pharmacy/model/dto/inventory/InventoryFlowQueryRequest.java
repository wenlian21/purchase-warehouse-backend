package com.pharmacy.model.dto.inventory;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存流水查询请求 DTO
 * 用于封装库存流水查询条件，支持分页
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryFlowQueryRequest extends PageRequest implements Serializable {

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 变动类型
     */
    private String changeType;

    /**
     * 操作员姓名
     */
    private String operatorName;

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
