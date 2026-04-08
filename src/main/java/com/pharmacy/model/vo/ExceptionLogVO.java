package com.pharmacy.model.vo;

import java.time.LocalDateTime;
/**
 * 异常日志视图对象
 */
public record ExceptionLogVO(Long id, String exceptionType, String exceptionMessage, String requestUrl, String userName,
        String ip, String handleStatus, LocalDateTime createTime) {
}
