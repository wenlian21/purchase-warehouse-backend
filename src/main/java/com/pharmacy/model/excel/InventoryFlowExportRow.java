package com.pharmacy.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryFlowExportRow {

    @ExcelProperty("药品名称")
    private String medicineName;

    @ExcelProperty("批次号")
    private String batchNo;

    @ExcelProperty("业务类型")
    private String bizType;

    @ExcelProperty("变动方向")
    private String changeType;

    @ExcelProperty("变动数量")
    private Integer quantity;

    @ExcelProperty("单价")
    private BigDecimal unitPrice;

    @ExcelProperty("关联单号")
    private String documentNo;

    @ExcelProperty("操作人")
    private String operatorName;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
