package com.pharmacy.model.vo;

import java.time.LocalDateTime;
/**
 * 分类视图对象
 */
public record CategoryVO(Long id, String categoryName, String categoryRemark, LocalDateTime createTime,
        LocalDateTime updateTime) {
}
