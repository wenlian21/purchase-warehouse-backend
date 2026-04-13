package com.pharmacy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pharmacy.model.dto.medicine.MedicineQueryRequest;
import com.pharmacy.model.dto.medicine.MedicineSaveRequest;
import com.pharmacy.model.entity.Medicine;
import com.pharmacy.model.vo.MedicineVO;
import java.util.List;
import java.util.Map;

public interface MedicineService extends IService<Medicine> {
/**
 * 保存药品信息
 *
 * @param request
 * @return
 */
    Long saveMedicine(MedicineSaveRequest request);

    /**
     * 构建药品查询条件包装器
     *
     * @param request 药品查询请求对象，包含查询条件
     * @return 查询条件包装器
     */
    QueryWrapper<Medicine> buildQueryWrapper(MedicineQueryRequest request);
/**
 * 将实体对象转换为VO对象
 *
 * @param entity      实体对象
 * @param categoryMap 分类名称映射表
 * @return 转换后的VO对象
 */
    MedicineVO toVO(Medicine entity, Map<Long, String> categoryMap);
/**
 * 将实体对象列表转换为VO对象列表
 *
 * @param records     实体对象列表
 * @param categoryMap 分类名称映射表
 * @return 转换后的VO对象列表
 */
    List<MedicineVO> toVOList(List<Medicine> records, Map<Long, String> categoryMap);
}
