package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
/**
 * 角色实体类
 * 对应数据库中的 role 表，用于存储系统角色信息
 */
@Data
@TableName("role")
public class Role implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;//角色ID

    private String roleName;//角色名称

    private String roleCode;//角色编码

    private String roleDesc;//角色描述

    private String menuPermissions;//菜单权限，JSON格式

    private String buttonPermissions;//按钮权限，JSON格式

    private Integer isSystem;//是否系统角色，0-否，1-是

    private LocalDateTime createTime;//创建时间

    private LocalDateTime updateTime;//更新时间

    @TableLogic
    private Integer isDelete;//逻辑删除，0-未删除，1-已删除

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;//序列化版本号
}
