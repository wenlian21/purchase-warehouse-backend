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
 * 药品实体类
 */
@Data
@TableName("medicine")
public class Medicine implements Serializable {

    /**
     * 药品 ID（主键自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 药品名称
     */
    private String medicineName;

    /**
     * 规格
     */
    private String specification;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 批准文号
     */
    private String approvalNumber;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 销售价格
     */
    private BigDecimal salePrice;

    /**
     * 有效期天数
     */
    private Integer shelfLifeDays;

    /**
     * 安全库存最小值
     */
    private Integer safeStockMin;

    /**
     * 安全库存最大值
     */
    private Integer safeStockMax;

    /**
     * 总库存
     */
    private Integer totalStock;

    /**
     * 是否处方药（0-否，1-是）
     */
    private Integer isPrescription;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 序列化版本号
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
