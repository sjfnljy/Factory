package com.sjf.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.constant.RedisPrefix;
import com.sjf.utils.JWTUtil;
import com.sjf.utils.RedisUtil;
import com.sjf.utils.ResponseUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description: 基于用户 token 实现登录的过滤器。
 * @Author: SJF
 * @Date: 2024/1/14
 **/
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;

    public TokenAuthenticationFilter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        // 登录接口、注销接口及获取验证码接口直接放行。
        if("/factory/admin/auth/login".equals(requestUri) || "/factory/admin/auth/logout".equals(requestUri) ||
                    "/factory/admin/auth/generate/validateCode".equals(requestUri)){
            filterChain.doFilter(request,response);
        }
        // 获取请求头中的 token 并进行校验。
        String token = request.getHeader("token");
        if(StrUtil.isNotBlank(token)){
            Long userId = JWTUtil.getUserIdByJwtToken(token);
            String username = JWTUtil.getUsernameByJwtToken(token);
            // 校验是否与缓存中的 token 一致。
            String tokenInRedis = (String) redisUtil.get(RedisPrefix.LOGIN_PREFIX + userId);
            if(tokenInRedis.equals(token)){
                // 校验一致则保存个人信息且更新用户缓存时间。
                redisUtil.setExpireTime(RedisPrefix.LOGIN_PREFIX + userId,120L);
                // 获取用户权限数据。
                String authorityString = (String) redisUtil.get(RedisPrefix.USER_AUTHORITY + userId);
                List<Map> mapList = JSON.parseArray(authorityString, Map.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Map map : mapList) {
                    authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
                }
                UsernamePasswordAuthenticationToken authenticationToken;
                // 根据权限集合创建对应的 AuthenticationToken 对象。
                if(CollUtil.isNotEmpty(authorities)){
                    authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                }else {
                    authenticationToken = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                }
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request,response);
            }
        }else{
            // 请求头未携带 token 时写回前端。
            ResponseUtil.out(response, Result.build(HttpCode.AUTHENTICATION_FAILURE,"请求头中未携带 token"));
        }
    }
}
