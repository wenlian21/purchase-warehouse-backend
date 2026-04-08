package com.pharmacy.model.dto.sales;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 销售查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SalesQueryRequest extends PageRequest {

    private String keyword;//关键词
}
