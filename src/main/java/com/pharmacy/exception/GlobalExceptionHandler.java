package com.pharmacy.exception;

import cn.hutool.json.JSONUtil;
import com.pharmacy.common.BaseResponse;
import com.pharmacy.common.ErrorCode;
import com.pharmacy.common.ResultUtils;
import com.pharmacy.model.entity.SystemExceptionLog;
import com.pharmacy.model.entity.User;
import com.pharmacy.service.SystemExceptionLogService;
import com.pharmacy.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * 全局异常处理器，用于统一处理系统中的各类异常
 * 通过@RestControllerAdvice 注解实现全局异常捕获
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final SystemExceptionLogService systemExceptionLogService;
    private final UserService userService;
    private final HttpServletRequest request;

    public GlobalExceptionHandler(SystemExceptionLogService systemExceptionLogService, UserService userService,
            HttpServletRequest request) {
        this.systemExceptionLogService = systemExceptionLogService;
        this.userService = userService;
        this.request = request;
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        saveExceptionLog(e);
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        saveExceptionLog(e);
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误，请联系管理员处理");
    }

    private void saveExceptionLog(Exception exception) {
        User loginUser = userService.getLoginUserPermitNull(request);
        SystemExceptionLog logRecord = new SystemExceptionLog();
        logRecord.setExceptionType(exception.getClass().getName());
        logRecord.setExceptionMessage(exception.getMessage());
        logRecord.setStackTrace(JSONUtil.toJsonStr(exception.getStackTrace()));
        logRecord.setRequestUrl(request.getRequestURI());
        logRecord.setRequestParams(JSONUtil.toJsonStr(request.getParameterMap()));
        logRecord.setUserId(loginUser == null ? null : loginUser.getId());
        logRecord.setUserName(loginUser == null ? null : loginUser.getUserName());
        logRecord.setIp(request.getRemoteAddr());
        logRecord.setHandleStatus("待处理");
        systemExceptionLogService.save(logRecord);
    }
}
