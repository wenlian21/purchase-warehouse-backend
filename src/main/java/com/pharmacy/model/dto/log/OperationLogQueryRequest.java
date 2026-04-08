package com.pharmacy.model.dto.log;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 操作日志查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLogQueryRequest extends PageRequest implements Serializable {

    private String keyword;//关键词

    private String moduleName;//模块名

    private String executeStatus;//执行状态

    private static final long serialVersionUID = 1L;//序列号
}
