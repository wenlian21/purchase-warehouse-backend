package com.pharmacy.model.dto.log;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 异常日志查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)//继承父类属性
public class ExceptionLogQueryRequest extends PageRequest implements Serializable {

    private String keyword;//关键词

    private String handleStatus;//处理状态

    private static final long serialVersionUID = 1L;//序列号
}
