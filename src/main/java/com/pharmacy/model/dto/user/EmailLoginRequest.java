package com.pharmacy.model.dto.user;

import java.io.Serializable;
import lombok.Data;
/**
 * 邮箱验证码发送请求
 */
@Data
public class EmailLoginRequest implements Serializable {

    private String email;//邮箱

    private String verifyCode;//验证码

    private static final long serialVersionUID = 1L;
}
