package com.pharmacy.model.vo;

import java.math.BigDecimal;
/**
 * 销量排名
 */
public record SalesRankingVO(String medicineName, Integer salesQuantity, BigDecimal grossProfit) {
}
