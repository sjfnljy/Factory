package com.sjf.common.log;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 通过 Import 注解导入日志切面类到 Spring 容器中
 * @Author: SJF
 * @Date: 2024/1/7 21:45
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = LogAspect.class)
public @interface EnableLogAspect {
}
