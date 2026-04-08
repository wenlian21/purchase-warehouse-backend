package com.pharmacy.model.dto.role;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
/**
 * 角色权限保存参数
 */
@Data
public class RoleSaveRequest implements Serializable {

    private Long id;// 角色ID

    private String roleName;// 角色名称

    private String roleCode;// 角色编码

    private String roleDesc;// 角色描述

    private List<String> menuPermissions;// 菜单权限

    private List<String> buttonPermissions;// 按钮权限

    private static final long serialVersionUID = 1L;// 序列化ID
}
