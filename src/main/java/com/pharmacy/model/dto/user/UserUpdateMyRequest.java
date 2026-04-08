package com.pharmacy.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新个人信息请求 DTO
 * 用于封装当前用户修改个人资料时提交的参数信息
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像 URL
     */
    private String userAvatar;

    /**
     * 个人简介
     */
    private String userProfile;

    private static final long serialVersionUID = 1L;
}
