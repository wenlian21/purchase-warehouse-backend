package com.pharmacy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pharmacy.model.entity.Medicine;
/**
 * 药品数据访问层接口
 * 继承 BaseMapper，提供对 Medicine 实体表的基础 CRUD 操作
 */
public interface MedicineMapper extends BaseMapper<Medicine> {
}
