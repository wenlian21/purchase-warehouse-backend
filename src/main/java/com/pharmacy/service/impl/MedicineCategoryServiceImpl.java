package com.pharmacy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pharmacy.common.ErrorCode;
import com.pharmacy.exception.BusinessException;
import com.pharmacy.exception.ThrowUtils;
import com.pharmacy.mapper.MedicineCategoryMapper;
import com.pharmacy.mapper.MedicineMapper;
import com.pharmacy.model.dto.category.CategoryQueryRequest;
import com.pharmacy.model.dto.category.CategorySaveRequest;
import com.pharmacy.model.entity.MedicineCategory;
import com.pharmacy.model.vo.CategoryVO;
import com.pharmacy.service.MedicineCategoryService;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class MedicineCategoryServiceImpl extends ServiceImpl<MedicineCategoryMapper, MedicineCategory>
        implements MedicineCategoryService {

    @Resource
    private MedicineMapper medicineMapper;

    /**
     * 保存分类
     *
     * @param request
     * @return
     */
    @Override
    public Long saveCategory(CategorySaveRequest request) {
        //抛出参数错误
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "分类参数不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(request.getCategoryName()), ErrorCode.PARAMS_ERROR, "分类名称不能为空");
        //创建分类对象
        QueryWrapper<MedicineCategory> queryWrapper = new QueryWrapper<MedicineCategory>()
                .eq("categoryName", request.getCategoryName());
        //判断分类id是否重复
        if (request.getId() != null) {
            queryWrapper.ne("id", request.getId());
        }
        ThrowUtils.throwIf(this.count(queryWrapper) > 0, ErrorCode.PARAMS_ERROR, "分类名称已存在");
        MedicineCategory category = new MedicineCategory();
        //拷贝
        BeanUtils.copyProperties(request, category);
        //保存或更新
        boolean result = request.getId() == null ? this.save(category) : this.updateById(category);
        //不是result 抛出异常
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, request.getId() == null ? "新增分类失败" : "更新分类失败");
        return category.getId() == null ? request.getId() : category.getId();
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(java.io.Serializable id) {
        //转为 Long
        Long categoryId = Long.valueOf(String.valueOf(id));
        //判断分类下是否有药品
        Long medicineCount = medicineMapper.selectCount(new QueryWrapper<com.pharmacy.model.entity.Medicine>()
                .eq("categoryId", categoryId));
        ThrowUtils.throwIf(medicineCount > 0, ErrorCode.OPERATION_ERROR, "分类下仍有药品，暂不可删除");
        return super.removeById(id);
    }

    /**
     * 构建查询条件
     *
     * @param request
     * @return
     */
    @Override
    public QueryWrapper<MedicineCategory> buildQueryWrapper(CategoryQueryRequest request) {

        QueryWrapper<MedicineCategory> queryWrapper = new QueryWrapper<>();
        //如果request为空，则按照时间降序排序
        if (request == null) {
            return queryWrapper.orderByDesc("updateTime");
        }
        //如果keyword不为空，则根据关键词模糊查询
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper.like("categoryName", request.getKeyword())
                    .or().like("categoryRemark", request.getKeyword()));
        }
        //按照时间降序排序
        return queryWrapper.orderByDesc("updateTime");
    }

    /**
     * 转换为VO
     *
     * @param entity
     * @return
     */
    @Override
    public CategoryVO toVO(MedicineCategory entity) {
        //抛出参数错误
        ThrowUtils.throwIf(entity == null, ErrorCode.NOT_FOUND_ERROR, "分类不存在");
        //返回VO
        return new CategoryVO(entity.getId(), entity.getCategoryName(), entity.getCategoryRemark(), entity.getCreateTime(),
                entity.getUpdateTime());
    }
}
