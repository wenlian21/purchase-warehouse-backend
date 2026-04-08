package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 邮箱验证码实体类
 * 对应数据库中的 emailVerifyCode 表，记录发送的邮箱验证码信息
 */
@Data
@TableName("emailVerifyCode")
public class EmailVerifyCode implements Serializable {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 业务类型（登录、注册等）
     */
    private String bizType;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 使用状态（0-未使用，1-已使用）
     */
    private Integer usedStatus;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
