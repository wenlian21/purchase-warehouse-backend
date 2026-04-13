package com.pharmacy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pharmacy.model.dto.category.CategoryQueryRequest;
import com.pharmacy.model.dto.category.CategorySaveRequest;
import com.pharmacy.model.entity.MedicineCategory;
import com.pharmacy.model.vo.CategoryVO;

public interface MedicineCategoryService extends IService<MedicineCategory> {
    /**
     * 保存分类信息
     *
     * @param request
     * @return
     */
    Long saveCategory(CategorySaveRequest request);

    /**
     * 构建查询条件
     *
     * @param request
     * @return
     */
    QueryWrapper<MedicineCategory> buildQueryWrapper(CategoryQueryRequest request);

    /**
     * 转换为VO对象
     *
     * @param entity
     * @return
     */
    CategoryVO toVO(MedicineCategory entity);
}
