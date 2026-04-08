package com.pharmacy.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
/**
 * 用户视图对象
 *
 */
@Data
public class UserVO implements Serializable {

    private Long id;// 用户ID

    private String userName;// 用户名称

    private String userAccount;// 用户账号

    private String employeeNo;// 员工编号

    private String phone;// 手机号

    private String email;// 邮箱

    private String department;// 部门

    private Long roleId;// 角色ID

    private String roleName;// 角色名称

    private String userRole;// 用户角色

    private String userStatus;// 用户状态

    private LocalDateTime createTime;// 创建时间

    private static final long serialVersionUID = 1L;// 序列化版本号
}
