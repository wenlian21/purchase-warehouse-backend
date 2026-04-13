package com.pharmacy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 权限检查注解
 *
 * 用于标识需要进行权限验证的方法或类，通过指定必须拥有的角色来进行访问控制
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})//方法或类级别
@Retention(RetentionPolicy.RUNTIME)//运行时注解
public @interface AuthCheck {
/**
 * 必须有该角色才能访问
 */
    String mustRole() default "";
}
