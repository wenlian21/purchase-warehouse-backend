package pharmacy.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 订单导出行数据
 */
@Data
@AllArgsConstructor
public class SalesOrderExportRow {

    @ExcelProperty("订单号")
    private String orderNo;

    @ExcelProperty("会员姓名")
    private String memberName;

    @ExcelProperty("会员手机号")
    private String memberPhone;

    @ExcelProperty("实收金额")
    private BigDecimal actualAmount;

    @ExcelProperty("优惠金额")
    private BigDecimal discountAmount;

    @ExcelProperty("积分")
    private Integer pointsEarned;

    @ExcelProperty("收银员")
    private String cashierName;

    @ExcelProperty("订单状态")
    private String orderStatus;

    @ExcelProperty("销售明细")
    private String itemSummary;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
