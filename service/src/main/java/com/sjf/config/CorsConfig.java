package com.sjf.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: 后端跨域配置类。
 * @Author: SJF
 * @Date: 2024/1/7 22:31
 */
@Component
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NotNull CorsRegistry registry) {
       registry.addMapping("/**")
               .allowCredentials(true)
               .allowedOriginPatterns("*")
               .allowedMethods("*")
               .allowedHeaders("*");
    }
}
