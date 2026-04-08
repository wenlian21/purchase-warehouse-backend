package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 采购订单明细实体类
 * 对应数据库中的 purchase_order_item 表，记录采购订单明细信息
 */
@Data
@TableName("purchase_order_item")
public class PurchaseOrderItem implements Serializable {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 采购订单 ID
     */
    private Long purchaseOrderId;

    /**
     * 药品 ID
     */
    private Long medicineId;

    /**
     * 药品名称快照
     */
    private String medicineName;

    /**
     * 采购数量
     */
    private Integer quantity;

    /**
     * 采购单价
     */
    private BigDecimal purchasePrice;

    /**
     * 行金额
     */
    private BigDecimal lineAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
