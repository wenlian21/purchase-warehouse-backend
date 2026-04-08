package com.pharmacy.model.dto.user;

import java.io.Serializable;
import lombok.Data;
/**
 * 邮箱验证码发送请求
 */
@Data
public class EmailCodeSendRequest implements Serializable {

    private String email;//邮箱地址

    private static final long serialVersionUID = 1L;//序列号
}
