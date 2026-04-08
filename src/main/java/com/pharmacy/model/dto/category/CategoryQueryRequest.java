package com.pharmacy.model.dto.category;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 药品分类查询请求对象
 * 用于分页查询分类列表时传递参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryQueryRequest extends PageRequest {

    private String keyword;//关键词
}
