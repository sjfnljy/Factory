package com.sjf.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: FastJSON 配置类
 * @Author: SJF
 * @Date: 2024/1/10 19:46
 */
@Configuration
public class MyFastJsonConfig {
    /**
     * 使用 FastJson 的注解 @JsonField 无效，需要加入改配置。
     * @return: 自定义的消息转换器。
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 创建FastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 创建FastJson配置对象
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        // 修改配置返回内容的过滤
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
        );

        // 设置日期格式
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // 将配置设置到转换器并添加到HttpMessageConverter转换器列表中
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }
}
