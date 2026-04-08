package com.pharmacy.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户登录请求 DTO
 * 用于封装用户登录时的账号和密码信息
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;
}
