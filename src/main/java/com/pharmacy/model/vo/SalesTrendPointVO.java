package com.pharmacy.model.vo;

import java.math.BigDecimal;
/**
 * 销量趋势点数据
 */
public record SalesTrendPointVO(String label, BigDecimal salesAmount, Integer salesQuantity) {
}
