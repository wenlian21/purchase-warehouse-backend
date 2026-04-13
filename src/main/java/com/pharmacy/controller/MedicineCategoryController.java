package com.pharmacy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pharmacy.common.BaseResponse;
import com.pharmacy.common.DeleteRequest;
import com.pharmacy.common.ResultUtils;
import com.pharmacy.model.dto.category.CategoryQueryRequest;
import com.pharmacy.model.dto.category.CategorySaveRequest;
import com.pharmacy.model.entity.MedicineCategory;
import com.pharmacy.model.vo.CategoryVO;
import com.pharmacy.service.MedicineCategoryService;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 药品分类控制器
 * 提供药品分类的保存、分页查询、删除等功能接口
 */
@RestController
@RequestMapping("/category")
public class MedicineCategoryController {

    @Resource
    private MedicineCategoryService medicineCategoryService;

    @PostMapping("/save")
    public BaseResponse<Long> saveCategory(@RequestBody CategorySaveRequest request) {
        return ResultUtils.success(medicineCategoryService.saveCategory(request));
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<CategoryVO>> listCategory(@RequestBody(required = false) CategoryQueryRequest request) {
        long current = request == null ? 1 : request.getCurrent();
        long pageSize = request == null ? 10 : request.getPageSize();
        Page<MedicineCategory> entityPage = medicineCategoryService.page(new Page<>(current, pageSize),
                medicineCategoryService.buildQueryWrapper(request));
        Page<CategoryVO> voPage = new Page<>(current, pageSize, entityPage.getTotal());
        voPage.setRecords(entityPage.getRecords().stream().map(medicineCategoryService::toVO).collect(Collectors.toList()));
        return ResultUtils.success(voPage);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCategory(@RequestBody DeleteRequest request) {
        return ResultUtils.success(medicineCategoryService.removeById(request.getId()));
    }
}
