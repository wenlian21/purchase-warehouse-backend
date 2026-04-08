package com.pharmacy.model.dto.user;

import java.io.Serializable;
import lombok.Data;
/**
 * 用户注册请求体
 *
 *
 *
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;// 序列化版本号

    private String userAccount;// 用户账号

    private String userPassword;// 用户密码

    private String checkPassword;// 校验密码
}
