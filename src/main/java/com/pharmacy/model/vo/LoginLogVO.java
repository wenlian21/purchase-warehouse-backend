package com.pharmacy.model.vo;

import java.time.LocalDateTime;
/**
 * 登录日志视图对象
 */
public record LoginLogVO(Long id, String userAccount, String userName, String loginType, String loginStatus,
        String failReason, String ip, LocalDateTime loginTime, LocalDateTime logoutTime,
        Long sessionDurationSeconds) {
}
