package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 库存批次实体类
 * 对应数据库中的 inventoryBatch 表，记录药品批次信息
 */
@Data
@TableName("inventoryBatch")
public class InventoryBatch implements Serializable {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 药品 ID
     */
    private Long medicineId;

    /**
     * 供应商 ID
     */
    private Long supplierId;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 有效期至
     */
    private LocalDate expiryDate;

    /**
     * 存储位置
     */
    private String storageLocation;

    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;

    /**
     * 可用库存数量
     */
    private Integer availableStock;

    /**
     * 批次状态
     */
    private String batchStatus;

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
