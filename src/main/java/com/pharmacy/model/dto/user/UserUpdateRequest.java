package com.pharmacy.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新请求 DTO
 * 用于封装管理员更新员工账号信息时提交的参数
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * 用户 ID
     */
    private Long id;

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
     * 所属部门
     */
    private String department;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 用户状态
     */
    private String userStatus;

    private static final long serialVersionUID = 1L;
}
