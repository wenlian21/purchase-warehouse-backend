package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
/**
 * 登录日志
 *
 */
@Data
@TableName("loginLog")
public class LoginLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;//主键

    private Long userId;//用户id

    private String userAccount;//用户账号

    private String userName;//用户名称

    private String loginType;//登录方式

    private String loginStatus;//登录状态

    private String failReason;//登录失败原因

    private String ip;//登录ip

    private LocalDateTime loginTime;//登录时间

    private LocalDateTime logoutTime;//登出时间

    private Long sessionDurationSeconds;//会话时长

    private LocalDateTime createTime;//创建时间

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;//序列化版本号
}
