package com.pharmacy.model.dto.supplier;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierQueryRequest extends PageRequest {

    private String keyword;

    private Integer isBlacklisted;

    private String ratingLevel;
}
