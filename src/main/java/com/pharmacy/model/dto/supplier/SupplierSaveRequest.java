package com.pharmacy.model.dto.supplier;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SupplierSaveRequest {

    private Long id;

    private String supplierCode;

    private String supplierName;

    private String contactPerson;

    private String contactPhone;

    private String qualificationNo;

    private LocalDateTime qualificationExpireAt;

    private String ratingLevel;

    private String settlementCycle;

    private String agreementPriceRemark;

    private Integer isBlacklisted;
}
