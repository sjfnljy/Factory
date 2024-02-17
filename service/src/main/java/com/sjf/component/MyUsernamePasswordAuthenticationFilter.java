package com.sjf.component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.constant.RedisPrefix;
import com.sjf.exception.CustomException;
import com.sjf.utils.RedisUtil;
import com.sjf.utils.ResponseUtil;
import com.sjf.vo.auth.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 用户名密码进行登录校验过滤器，登陆成功则生成 token 存入缓存中。
 * @Author: SJF
 * @Date: 2023/9/2
 **/
@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final RedisUtil redisUtil;

    /**
     * 构造注入 AuthenticationManager 和 RedisUtil，因为过滤器中无法使用注解注入方式。
     * @param authenticationManager: 注入的 AuthenticationManager,用于登录认证。
     * @param redisUtil: 注入的 RedisUtil,用于实现缓存功能。
     */
    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, RedisUtil redisUtil) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        // 指定登录接口及提交方式，可以指定任意路径。
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/auth/login", "POST"));
        this.redisUtil = redisUtil;
    }

    /**
     * 用户名密码登录认证方法。
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Logger logger = LoggerFactory.getLogger(MyUsernamePasswordAuthenticationFilter.class);

        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            if(loginVo == null){
                logger.error("LoginVo conversion failed");
                throw new CustomException(HttpCode.AUTHENTICATION_FAILURE,"转换用户信息失败");
            }
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            // 登录认证。
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e){
            throw new CustomException(HttpCode.AUTHENTICATION_FAILURE, "获取用户名、密码失败");
        }
    }

    /**
     * 用户名密码登录认证自动调用该方法。
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)  {
        // 获取认证成功的用户信息。
        AuthUser authUser = (AuthUser) authResult.getPrincipal();
        if(authUser == null){
            throw new CustomException(HttpCode.AUTHENTICATION_FAILURE,"获取认证成功的用户信息失败");
        }
        Long userId = authUser.getSystemUser().getId();
        // 缓存中写入用户对应的权限信息,并设置默认过期时间为 2 小时。
        redisUtil.set(RedisPrefix.USER_AUTHORITY + userId, JSON.toJSONString(authUser.getAuthorities()),120L);
        System.out.println(redisUtil.get(RedisPrefix.USER_AUTHORITY + userId));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        // 认证失败向前端返回认证失败原因。
        String message = failed.getMessage();
        ResponseUtil.out(response, Result.build(HttpCode.AUTHENTICATION_FAILURE,message));
    }
}
