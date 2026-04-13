package com.pharmacy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pharmacy.common.BaseResponse;
import com.pharmacy.common.DeleteRequest;
import com.pharmacy.common.ResultUtils;
import com.pharmacy.model.dto.medicine.MedicineQueryRequest;
import com.pharmacy.model.dto.medicine.MedicineSaveRequest;
import com.pharmacy.model.entity.Medicine;
import com.pharmacy.model.entity.MedicineCategory;
import com.pharmacy.model.vo.MedicineVO;
import com.pharmacy.service.MedicineCategoryService;
import com.pharmacy.service.MedicineService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
*药物管理
* 提供药物管理相关接口
*/
@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Resource
    private MedicineService medicineService;

    @Resource
    private MedicineCategoryService medicineCategoryService;
/**
    * 保存药物信息
    * @param request
    * @return
    */
    @PostMapping("/save")
    public BaseResponse<Long> saveMedicine(@RequestBody MedicineSaveRequest request) {
        return ResultUtils.success(medicineService.saveMedicine(request));
    }
/**
    * 分页查询药物列表
    * @param request
    * @return
    */
    @PostMapping("/list/page")
    public BaseResponse<Page<MedicineVO>> listMedicine(@RequestBody(required = false) MedicineQueryRequest request) {
        //如果请求为空，则默认为第1页，每页10条数据
        long current = request == null ? 1 : request.getCurrent();
        long pageSize = request == null ? 10 : request.getPageSize();
        //调用分页查询，获取结果
        Page<Medicine> medicinePage = medicineService.page(new Page<>(current, pageSize),
                medicineService.buildQueryWrapper(request));
        //获取分类名称
        List<Long> categoryIds = medicinePage.getRecords().stream().map(Medicine::getCategoryId).distinct().collect(Collectors.toList());
        Map<Long, String> categoryMap = categoryIds.isEmpty() ? Map.of()
                : medicineCategoryService.listByIds(categoryIds).stream().collect(Collectors.toMap(MedicineCategory::getId,
                        MedicineCategory::getCategoryName));
        //创建结果对象，转换为VO
        Page<MedicineVO> result = new Page<>(current, pageSize, medicinePage.getTotal());
        result.setRecords(medicineService.toVOList(medicinePage.getRecords(), categoryMap));
        return ResultUtils.success(result);
    }
/**
    * 删除药物信息
    * @param request
    * @return
    */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMedicine(@RequestBody DeleteRequest request) {
        return ResultUtils.success(medicineService.removeById(request.getId()));
    }
}
