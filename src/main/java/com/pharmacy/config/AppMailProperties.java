package com.pharmacy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * 邮件服务配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.mail")// 表示从 application.yml中读取以 "app.mail" 为前缀的邮件相关配置
public class AppMailProperties {

    /**
     * 是否启用邮件发送模拟模式
     * true 表示不实际发送邮件，仅记录日志；false 表示真实发送邮件
     * 默认值为 true，适用于开发测试环境
     */
    private boolean mock = true;
    /**
     * 验证码有效期（分钟）
     * 指定生成的邮箱验证码在多长时间内有效，超时后自动失效
     * 默认值为 5 分钟
     */
    private int codeExpireMinutes = 5;
    /**
     * 验证码发送冷却时间（秒）
     * 两次验证码发送请求之间的最小时间间隔，防止频繁请求
     * 默认值为 60 秒
     */
    private int codeCooldownSeconds = 60;
}
