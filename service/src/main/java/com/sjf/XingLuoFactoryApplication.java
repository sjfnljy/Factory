package com.sjf;

import com.sjf.common.log.EnableLogAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description: 数字化工厂启动类
 * @Author: SJF
 * @Date: 2024/1/7 19:58
 */
@SpringBootApplication
@EnableLogAspect
@EnableAsync
public class XingLuoFactoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(XingLuoFactoryApplication.class);
    }
}
