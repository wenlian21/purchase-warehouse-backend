package com.pharmacy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pharmacy.common.ErrorCode;
import com.pharmacy.exception.ThrowUtils;
import com.pharmacy.mapper.InventoryBatchMapper;
import com.pharmacy.mapper.MedicineMapper;
import com.pharmacy.mapper.SalesOrderItemMapper;
import com.pharmacy.model.dto.medicine.MedicineQueryRequest;
import com.pharmacy.model.dto.medicine.MedicineSaveRequest;
import com.pharmacy.model.entity.InventoryBatch;
import com.pharmacy.model.entity.Medicine;
import com.pharmacy.model.vo.MedicineVO;
import com.pharmacy.service.MedicineService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 药品服务实现类
 */
@Service
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine> implements MedicineService {
    // 药品条形码正则表达式
    private static final Pattern BARCODE_PATTERN = Pattern.compile("^[0-9]{8,18}$");
    // 库存批次Mapper
    private final InventoryBatchMapper inventoryBatchMapper;
    // 销售订单项Mapper
    private final SalesOrderItemMapper salesOrderItemMapper;

    //用于在删除药品时检查是否存在库存批次记录和销售记录
    public MedicineServiceImpl(InventoryBatchMapper inventoryBatchMapper, SalesOrderItemMapper salesOrderItemMapper) {
        this.inventoryBatchMapper = inventoryBatchMapper;
        this.salesOrderItemMapper = salesOrderItemMapper;
    }

    /**
     * @param request
     * @return
     * @description: 新增或更新
     */
    @Override
    public Long saveMedicine(MedicineSaveRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "药品参数不能为空");
        ThrowUtils.throwIf(request.getCategoryId() == null, ErrorCode.PARAMS_ERROR, "请选择药品分类");
        ThrowUtils.throwIf(StringUtils.isAnyBlank(request.getMedicineName(), request.getSpecification(),
                request.getManufacturer(), request.getBarcode()), ErrorCode.PARAMS_ERROR, "请完善药品基础信息");
        ThrowUtils.throwIf(request.getSalePrice() == null || request.getSalePrice().signum() < 0, ErrorCode.PARAMS_ERROR,
                "销售单价不合法");
        ThrowUtils.throwIf(request.getShelfLifeDays() == null || request.getShelfLifeDays() <= 0, ErrorCode.PARAMS_ERROR,
                "保质期必须大于0天");
        ThrowUtils.throwIf(!BARCODE_PATTERN.matcher(request.getBarcode()).matches(), ErrorCode.PARAMS_ERROR,
                "条形码必须为8到18位数字");
        checkUnique("medicineName", request.getMedicineName(), request.getId(), "药品名称已存在");
        checkUnique("barcode", request.getBarcode(), request.getId(), "条形码已存在");
        //创建药品对象
        Medicine medicine = new Medicine();
        //拷贝属性
        BeanUtils.copyProperties(request, medicine);
        if (medicine.getUnitName() == null) {
            medicine.setUnitName("盒");
        }
        if (medicine.getSafeStockMin() == null) {
            medicine.setSafeStockMin(0);
        }
        if (medicine.getSafeStockMax() == null) {
            medicine.setSafeStockMax(0);
        }
        if (medicine.getStatus() == null) {
            medicine.setStatus("启用");
        }
        if (medicine.getIsPrescription() == null) {
            medicine.setIsPrescription(0);
        }
        if (medicine.getTotalStock() == null) {
            medicine.setTotalStock(0);
        }
        //根据ID判断是新增还是更新
        boolean result = request.getId() == null ? this.save(medicine) : this.updateById(medicine);
        //判断操作结果
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, request.getId() == null ? "新增药品失败" : "更新药品失败");
        //返回药品ID
        return medicine.getId() == null ? request.getId() : medicine.getId();
    }

    /**
     * 删除药品（检查关联数据）
     *
     * @param id 药品 ID
     * @return 是否删除成功
     */
    @Override
    public boolean removeById(Serializable id) {
        //将id转为Long类型
        Long medicineId = Long.valueOf(String.valueOf(id));
        //数据完整性的校验
        ThrowUtils.throwIf(inventoryBatchMapper.selectCount(new QueryWrapper<InventoryBatch>().eq("medicineId", medicineId)) > 0,
                ErrorCode.OPERATION_ERROR, "药品已存在库存批次记录，不能删除");
        ThrowUtils.throwIf(salesOrderItemMapper.selectCount(new QueryWrapper<com.pharmacy.model.entity.SalesOrderItem>()
                .eq("medicineId", medicineId)) > 0, ErrorCode.OPERATION_ERROR, "药品已产生销售记录，不能删除");
        return super.removeById(id);
    }

    /**
     * 检查字段值的唯一性
     *
     * @param column  要检查的列名
     * @param value   要检查的值
     * @param id      当前记录 ID（更新时排除自身）
     * @param message 重复时的错误消息
     */
    private void checkUnique(String column, String value, Long id, String message) {
        // 创建查询条件
        QueryWrapper<Medicine> queryWrapper = new QueryWrapper<Medicine>().eq(column, value);
        if (id != null) {
            // 排除自身
            queryWrapper.ne("id", id);
        }
        //若存在重复记录则抛出异常
        ThrowUtils.throwIf(this.count(queryWrapper) > 0, ErrorCode.PARAMS_ERROR, message);
    }

    /**
     * 构建药品查询条件包装器
     *
     * @param request 药品查询请求对象，包含查询条件
     * @return 查询条件包装器
     */
    @Override

    public QueryWrapper<Medicine> buildQueryWrapper(MedicineQueryRequest request) {
        //创建查询条件包装器
        QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
        if (request == null) {
            // 如果没有查询条件，则返回默认的排序条件--默认按照更新时间倒序排序
            return queryWrapper.orderByDesc("updateTime");
        }
        // 根据关键字进行模糊查询
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper.like("medicineName", request.getKeyword())
                    .or().like("barcode", request.getKeyword())
                    .or().like("manufacturer", request.getKeyword()));
        }
        //为空不添加数据，否则添加数据
        queryWrapper.eq(request.getCategoryId() != null, "categoryId", request.getCategoryId());
        queryWrapper.eq(request.getIsPrescription() != null, "isPrescription", request.getIsPrescription());
        queryWrapper.eq(StringUtils.isNotBlank(request.getStatus()), "status", request.getStatus());
       //根据更新时间倒序排序
        return queryWrapper.orderByDesc("updateTime");
    }

    /**
     * 将药品实体转换为视图对象
     *
     * @param entity      药品实体
     * @param categoryMap 分类 ID 到分类名称的映射
     * @return 药品视图对象
     */
    @Override
    public MedicineVO toVO(Medicine entity, Map<Long, String> categoryMap) {
        //创建药品视图对象，把数据复制到视图对象中
        return new MedicineVO(entity.getId(), entity.getCategoryId(), categoryMap.get(entity.getCategoryId()),
                entity.getMedicineName(), entity.getSpecification(), entity.getManufacturer(), entity.getBarcode(),
                entity.getApprovalNumber(), entity.getUnitName(), entity.getSalePrice(), entity.getShelfLifeDays(),
                entity.getSafeStockMin(), entity.getSafeStockMax(), entity.getTotalStock(), entity.getIsPrescription(),
                entity.getStatus(), entity.getCreateTime());
    }

    /**
     * 将药品实体列表批量转换为视图对象列表
     *
     * @param records     药品实体列表
     * @param categoryMap 分类 ID 到分类名称的映射
     * @return 药品视图对象列表
     */
    @Override
    public List<MedicineVO> toVOList(List<Medicine> records, Map<Long, String> categoryMap) {
        //将实体列表转换为流，并使用map方法将每个实体转换为视图对象,然后将结果收集到一个list中
        return records.stream().map(item -> toVO(item, categoryMap)).collect(Collectors.toList());
    }
}
