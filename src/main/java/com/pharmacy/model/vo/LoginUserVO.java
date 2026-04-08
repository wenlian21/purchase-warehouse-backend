package com.pharmacy.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
/**
 * 登录用户信息
 */
@Data
public class LoginUserVO implements Serializable {

    private Long id;// 用户ID

    private String userName;// 用户名

    private String userAccount;// 用户账户

    private String employeeNo;// 员工编号

    private String phone;// 手机号

    private String email;// 邮箱

    private String department;// 部门

    private Long roleId;// 角色ID

    private String roleName;// 角色名称

    private String userRole;// 用户角色

    private String userStatus;// 用户状态

    private List<String> menuPermissions;// 菜单权限

    private List<String> buttonPermissions;// 按钮权限

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间

    private static final long serialVersionUID = 1L;// 序列化版本号
}
