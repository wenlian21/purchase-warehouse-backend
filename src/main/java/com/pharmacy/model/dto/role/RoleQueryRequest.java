package com.pharmacy.model.dto.role;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 角色查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)//继承父类属性
public class RoleQueryRequest extends PageRequest implements Serializable {

    private String keyword;//关键词

    private static final long serialVersionUID = 1L;//版本序列号
}
