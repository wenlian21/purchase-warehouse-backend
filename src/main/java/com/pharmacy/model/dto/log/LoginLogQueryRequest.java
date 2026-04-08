package com.pharmacy.model.dto.log;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 登录日志查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginLogQueryRequest extends PageRequest implements Serializable {

    private String keyword;//关键词

    private String loginStatus;//登录状态

    private String loginType;//登录方式

    private static final long serialVersionUID = 1L;//序列号
}
