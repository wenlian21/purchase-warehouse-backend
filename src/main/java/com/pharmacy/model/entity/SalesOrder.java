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
 * 订单表
 */
@Data
@TableName("salesOrder")
public class SalesOrder implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;//订单ID

    private String orderNo;//订单编号

    private String memberName;//会员名称

    private String memberPhone;//会员手机号

    private BigDecimal discountRate;//折扣率

    private BigDecimal originalAmount;// 原始金额

    private BigDecimal totalAmount;// 订单金额

    private BigDecimal discountAmount;//折扣金额

    private BigDecimal actualAmount;//实际金额

    private Integer pointsEarned;//积分

    private Integer pointsUsed;//使用积分

    private Integer prescriptionChecked;//处方审核状态

    private String cashierName;//收银员名称

    private String orderStatus;//订单状态

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间

    @TableLogic
    private Integer isDelete;//逻辑删除

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;// 序列化版本号
}
