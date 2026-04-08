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
 * 库存流水实体类
 * 对应数据库中的 inventoryFlow 表，记录库存变动明细
 */
@Data
@TableName("inventoryFlow")
public class InventoryFlow implements Serializable {

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
     * 批次 ID
     */
    private Long batchId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 变动类型（入库/出库）
     */
    private String changeType;

    /**
     * 变动数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 单据编号
     */
    private String documentNo;

    /**
     * 操作员姓名
     */
    private String operatorName;

    /**
     * 备注信息
     */
    private String remark;

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
