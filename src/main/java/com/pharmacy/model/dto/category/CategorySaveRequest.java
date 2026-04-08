package com.pharmacy.model.dto.category;

import lombok.Data;
/**
 * 药品分类保存请求对象
 * 用于新增或修改分类信息时传递参数
 */
@Data
public class CategorySaveRequest {

    private Long id;//分类ID

    private String categoryName;//分类名称

    private String categoryRemark;//分类备注
}
