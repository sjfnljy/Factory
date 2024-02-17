package com.sjf.config;

import com.sjf.component.MyPasswordEncoder;
import com.sjf.component.TokenAuthenticationFilter;
import com.sjf.utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @Description: Spring Security 配置类，使用 5.7 版本的新配置方法。
 * @Author: SJF
 * @Date: 2024/1/14
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AuthenticationConfiguration auth;

    @Resource
    private RedisUtil redisUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }

    /**
     * 获取AuthenticationManager（认证管理器），登录时认证使用
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return auth.getAuthenticationManager();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                // 基于 token，不需要 csrf。
                .csrf().disable()
                // 开启 cors，允许跨域访问。
                .cors()
                .and()
                // 基于 token，不需要 session。
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 请求认证管理：部分请求直接放行，其他必须要认证。
                .authorizeRequests()
                .anyRequest().permitAll()
                //.anyRequest().authenticated()
                .and()
                // 基于 URL 的注销处理方式。
                .logout()
                .logoutUrl("/factory/admin/auth/logout")
                .and()
                // 添加 JWT 过滤器，JWT 过滤器在用户名密码认证过滤器之前。
                .addFilterBefore(new TokenAuthenticationFilter(redisUtil), UsernamePasswordAuthenticationFilter.class)
                // 替换原有的用户名密码登录过滤器。
                //.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 替换原有的 UserDetailsService。
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 配置放行的静态资源。
        return (web) -> web.ignoring().antMatchers("/swagger**/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**",
                "/v2/**",
                "/doc.html",
                "/factory/admin/auth/login");
    }
}
