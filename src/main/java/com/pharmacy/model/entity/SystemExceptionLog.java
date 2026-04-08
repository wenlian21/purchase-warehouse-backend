package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
/**
 * 系统异常日志
 */
@Data
@TableName("systemExceptionLog")
public class SystemExceptionLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;// 主键

    private String exceptionType;// 异常类型

    private String exceptionMessage;// 异常消息

    private String stackTrace;// 堆栈信息

    private String requestUrl;// 请求地址

    private String requestParams;// 请求参数

    private Long userId;// 用户id

    private String userName;// 用户名

    private String ip;//用户 ip

    private String handleStatus;// 处理状态

    private String remark;// 备注

    private LocalDateTime createTime;// 创建时间

    @TableField(exist = false)// 字段不存在
    private static final long serialVersionUID = 1L;// 序列化版本号
}
