package com.pharmacy.model.vo;

import java.time.LocalDateTime;
import java.util.List;
/**
 * 角色视图对象
 */
public record RoleVO(Long id, String roleName, String roleCode, String roleDesc, List<String> menuPermissions,
        List<String> buttonPermissions, Integer isSystem, LocalDateTime createTime) {
}
