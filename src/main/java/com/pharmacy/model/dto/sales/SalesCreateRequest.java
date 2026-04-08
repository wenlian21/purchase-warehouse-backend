package com.pharmacy.model.dto.sales;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
/**
 * 销售创建请求
 */
@Data
public class SalesCreateRequest {

    private String memberName;//会员名称

    private String memberPhone;//会员电话

    private BigDecimal discountRate;//折扣率

    private Integer prescriptionChecked;//是否需要处方

    private String cashierName;//收银员名称

    private List<SalesItemRequest> items;//销售项

    @Data
    public static class SalesItemRequest {
        private Long medicineId;//药品ID
        private Integer quantity;// 数量
    }
}
