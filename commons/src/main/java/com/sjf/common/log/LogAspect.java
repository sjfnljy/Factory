package com.sjf.common.log;

import com.sjf.entity.log.OperationLog;
import com.sjf.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 环绕通知切面类
 * @Author: SJF
 * @Date: 2024/1/19 20:15
 */

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private OperationLogService asyncOperLogService ;


    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint , Log sysLog) {
        // 构建前置参数
        OperationLog sysOperLog = new OperationLog() ;

        LogUtil.beforeHandleLog(sysLog , joinPoint , sysOperLog) ;

        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
            // 执行业务方法
            LogUtil.afterHandleLog(sysLog , proceed , sysOperLog , 0 , null) ;
            // 构建响应结果参数
        } catch (Throwable e) {
            // 业务方法执行产生异常
            LogUtil.afterHandleLog(sysLog , proceed , sysOperLog , 1 , e.getMessage()) ;
            throw new RuntimeException();
        }

        // 保存日志数据
        asyncOperLogService.saveSysOperLog(sysOperLog);

        // 返回执行结果
        return proceed ;
    }
}
