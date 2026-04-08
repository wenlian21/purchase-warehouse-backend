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
 * 采购订单实体类
 * 对应数据库中的 purchase_order 表，记录采购订单基本信息
 */
@Data
@TableName("purchase_order")
public class PurchaseOrder implements Serializable {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 采购单号
     */
    private String orderNo;

    /**
     * 供应商 ID
     */
    private Long supplierId;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 总数量
     */
    private Integer totalCount;

    /**
     * 订单状态（待审核/审核通过/审核不通过/已完成）
     */
    private String orderStatus;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 审核人 ID
     */
    private Long auditorId;

    /**
     * 审核人姓名
     */
    private String auditorName;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

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
