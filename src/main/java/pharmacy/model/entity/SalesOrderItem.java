package pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 订单项
 *
 * @TableName salesOrderItem
 */
@Data
@TableName("salesOrderItem")
public class SalesOrderItem implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;// 订单项ID

    private Long salesOrderId;// 订单ID

    private Long medicineId;// 药品ID

    private String medicineName;// 药品名称

    private String batchNos;// 批次号

    private Integer quantity;// 数量

    private BigDecimal unitPrice;// 单价

    private BigDecimal lineAmount;// 金额

    private BigDecimal costAmount;// 成本价

    private BigDecimal grossProfit;// 毛利

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间

    @TableLogic
    private Integer isDelete;// 逻辑删除

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;// 序列化ID
}
