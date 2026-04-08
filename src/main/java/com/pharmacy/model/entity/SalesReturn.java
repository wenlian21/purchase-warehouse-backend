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
 * 退货单
 * @TableName salesReturn
 */
@Data
@TableName("salesReturn")
public class SalesReturn implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;//主键

    private Long salesOrderId;//订单ID

    private String returnNo;//退货单号

    private BigDecimal returnAmount;//退货金额

    private String returnReason;//退货原因

    private String operatorName;//操作员名称

    private LocalDateTime createTime;//创建时间

    private LocalDateTime updateTime;//修改时间

    @TableLogic
    private Integer isDelete;//逻辑删除

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;//版本号
}
