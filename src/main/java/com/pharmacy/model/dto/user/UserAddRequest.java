package com.pharmacy.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户新增请求 DTO
 * 用于封装管理员创建员工账号时提交的参数信息
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 姓名
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

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
     * 员工编号
     */
    private String employeeNo;

    /**
     * 角色 ID
     */
    private Long roleId;

    private static final long serialVersionUID = 1L;
}
