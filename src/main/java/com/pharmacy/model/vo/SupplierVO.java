package com.pharmacy.model.vo;

import java.time.LocalDateTime;
/*
*供应商视图对象
*/
public record SupplierVO(Long id, String supplierCode, String supplierName, String contactPerson, String contactPhone,
        String qualificationNo, LocalDateTime qualificationExpireAt, String ratingLevel, String settlementCycle,
        String agreementPriceRemark, Integer isBlacklisted, LocalDateTime createTime) {
}
