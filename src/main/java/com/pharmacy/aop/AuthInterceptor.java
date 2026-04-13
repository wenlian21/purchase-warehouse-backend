package com.pharmacy.aop;

import com.pharmacy.annotation.AuthCheck;
import com.pharmacy.common.ErrorCode;
import com.pharmacy.constant.UserConstant;
import com.pharmacy.exception.BusinessException;
import com.pharmacy.model.entity.User;
import com.pharmacy.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 权限校验 AOP
 * <p>
 * 基于 Spring AOP 实现，拦截标注了 {@link AuthCheck} 注解的方法或类，
 * 自动进行用户角色权限验证，确保只有具备指定角色的用户才能执行相应操作
 */
@Aspect// 表示这是一个切面 ，@Aspect 使 AuthInterceptor 能够拦截所有标注了 @AuthCheck 注解的方法，自动进行角色权限验证。
@Component// 表示这是一个组件，注入bean 到spring容器中
public class AuthInterceptor {

    @Resource// 表示注入bean
    private UserService userService;

    @Around("@annotation(authCheck) || @within(authCheck)")// 表示拦截所有标注了 @AuthCheck 注解的方法或类，自动进行角色权限验证。
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //如果当前方法或类没有标注 @AuthCheck 注解，则直接通过
        if (authCheck == null) {
            return joinPoint.proceed();
        }
        // 获取当前登录用户
        String mustRole = authCheck.mustRole();
        //如果当前用户没有指定角色，则直接通过
        if (mustRole == null || mustRole.isBlank()) {
            return joinPoint.proceed();
        }
 /**
 * 获取当前请求的登录用户信息
 * 从 RequestContextHolder 中获取当前请求上下文，进而获取 HttpServletRequest 对象
 * 通过 UserService 获取已登录的用户信息
 */
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User loginUser = userService.getLoginUser(request);
        /**
         * 判断当前用户是否为系统管理员
         * 如果是系统管理员，则直接通过
         */
        if (UserConstant.SYSTEM_ADMIN_ROLE.equals(loginUser.getUserRole())) {
            return joinPoint.proceed();
        }
        /**
         * 验证当前用户是否具备指定角色权限
         * 如果不具备，则抛出异常
         */
        if (!mustRole.equals(loginUser.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "暂无该操作权限");
        }
        return joinPoint.proceed();
    }
}
