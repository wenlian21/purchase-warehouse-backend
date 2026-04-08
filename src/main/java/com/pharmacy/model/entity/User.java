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
 * 用户实体类
 * 对应数据库中的 user 表，记录系统用户基本信息
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码（加密存储）
     */
    private String userPassword;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 员工编号
     */
    private String employeeNo;

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
     * 用户角色标识
     */
    private String userRole;

    /**
     * 头像 URL
     */
    private String userAvatar;

    /**
     * 个人简介
     */
    private String userProfile;

    /**
     * 用户状态
     */
    private String userStatus;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
