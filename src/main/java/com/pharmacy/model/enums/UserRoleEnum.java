package com.pharmacy.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
/**
 * 用户角色枚举
 * 定义系统中所有可用的角色类型及其对应的角色标识
 */
public enum UserRoleEnum {

    SYSTEM_ADMIN("系统管理员", UserConstant.SYSTEM_ADMIN_ROLE),
    STORE_MANAGER("店长", UserConstant.STORE_MANAGER_ROLE),
    INVENTORY_MANAGER("库管员", UserConstant.INVENTORY_MANAGER_ROLE),
    SALES_CLERK("销售员", UserConstant.SALES_CLERK_ROLE);

    private final String text;
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(UserRoleEnum::getValue).collect(Collectors.toList());
    }

    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum roleEnum : values()) {
            if (roleEnum.value.equals(value)) {
                return roleEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
