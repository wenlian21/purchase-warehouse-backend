package com.pharmacy.model.vo;

import java.time.LocalDateTime;
/**
 * 操作日志视图对象
 */
public record OperationLogVO(Long id, String moduleName, String operationType, String description, String requestMethod,
        String requestUrl, String operatorName, String ip, String executeStatus, Long durationMs,
        LocalDateTime createTime) {
}
