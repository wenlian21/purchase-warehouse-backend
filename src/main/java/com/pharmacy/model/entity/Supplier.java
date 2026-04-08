package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 供应商实体类
 * 对应数据库中的 supplier 表，记录供应商基本信息
 */
@Data
@TableName("supplier")
public class Supplier implements Serializable {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 联系人姓名
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 资质证号
     */
    private String qualificationNo;

    /**
     * 资质到期时间
     */
    private LocalDateTime qualificationExpireAt;

    /**
     * 评级等级
     */
    private String ratingLevel;

    /**
     * 结算周期
     */
    private String settlementCycle;

    /**
     * 协议价格备注
     */
    private String agreementPriceRemark;

    /**
     * 是否黑名单（0-否，1-是）
     */
    private Integer isBlacklisted;

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
