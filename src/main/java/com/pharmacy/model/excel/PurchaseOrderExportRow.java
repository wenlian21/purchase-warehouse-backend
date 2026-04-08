package com.pharmacy.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 采购订单导出 Excel 行
 */
@Data
@AllArgsConstructor
public class PurchaseOrderExportRow {

    @ExcelProperty("采购单号")
    private String orderNo;

    @ExcelProperty("供应商名称")
    private String supplierName;

    @ExcelProperty("药品名称")
    private String medicineName;

    @ExcelProperty("规格")
    private String specification;

    @ExcelProperty("采购数量")
    private Integer quantity;

    @ExcelProperty("采购单价")
    private BigDecimal purchasePrice;

    @ExcelProperty("行金额")
    private BigDecimal lineAmount;

    @ExcelProperty("订单状态")
    private String orderStatus;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
