package com.sjf.utils;

import com.alibaba.fastjson2.JSON;
import com.sjf.common.log.Log;
import com.sjf.entity.log.OperationLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Description: 日志工具类
 * @Author: SJF
 * @Date: 2024/1/19 20:26
 */
public final class LogUtil {

    // 操作执行之后调用
    public static void afterHandleLog(Log sysLog, Object proceed,
                                      OperationLog operationLog, int status ,
                                      String errorMsg) {
        if(sysLog.isSaveResponseData()) {
            operationLog.setJsonResult(JSON.toJSONString(proceed));
        }
        operationLog.setStatus(status);
        operationLog.setErrorMsg(errorMsg);
    }

    // 操作执行之前调用
    public static void beforeHandleLog(Log sysLog,
                                       ProceedingJoinPoint joinPoint,
                                       OperationLog operationLog) {

        // 设置操作模块名称。
        operationLog.setTitle(sysLog.title());
        operationLog.setOperatorType(sysLog.operatorType().name());

        // 获取目标方法信息。
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature() ;
        Method method = methodSignature.getMethod();
        operationLog.setMethod(method.getDeclaringClass().getName());

        // 获取请求相关参数。
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        operationLog.setRequestMethod(request.getMethod());
        operationLog.setOperationUrl(request.getRequestURI());
        operationLog.setOperationIp(request.getRemoteAddr());

        // 设置请求参数。
        if(sysLog.isSaveRequestData()) {
            String requestMethod = operationLog.getRequestMethod();
            if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
                String params = Arrays.toString(joinPoint.getArgs());
                operationLog.setOperationParam(params);
            }
        }
        String token = request.getHeader("token");
        operationLog.setOperationName(JWTUtil.getUsernameByJwtToken(token));
    }
}
