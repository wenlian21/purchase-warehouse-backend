package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
/**
 * 操作日志
 */
@Data
@TableName("operationLog")
public class OperationLogRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;//主键

    private String traceId;//链路追踪ID

    private String moduleName;//模块名称

    private String operationType;//操作类型

    private String description;//描述

    private String requestMethod;//请求方法

    private String requestUrl;//请求URL

    private String requestParams;//请求参数

    private String responseData;//响应数据

    private Long operatorId;//操作人ID

    private String operatorName;//操作人名称

    private String ip;// IP

    private String executeStatus;//执行状态

    private Long durationMs;//执行时长(毫秒)

    private LocalDateTime createTime;//创建时间

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;//序列化ID
}
