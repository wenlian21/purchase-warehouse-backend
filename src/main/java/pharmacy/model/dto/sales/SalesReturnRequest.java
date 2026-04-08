package pharmacy.model.dto.sales;

import lombok.Data;

import java.io.Serializable;
/**
 * 销售退货请求参数
 */
@Data
public class SalesReturnRequest implements Serializable {

    private Long salesOrderId;//销售订单ID

    private String operatorName;//操作员名称

    private String returnReason;//退货原因

    private static final long serialVersionUID = 1L;//序列化ID
}
