package com.sjf.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Description: JWT 工具类
 * @Author: SJF
 * @Date: 2024/1/13 13:21
 */

@Slf4j
public final class JWTUtil {
    /**
     * 两个常量： 过期时间；秘钥
     */
    public static final long EXPIRE = 1000*60*60*24;
    public static final String SECRET = "123456";

    /**
     * 跟据用户 id 和用户名生成 token 字符串的方法
     * @param userId: 用户 id
     * @param username: 用户名
     * @return: 生成的 token
     */
    public static String createJwtToken(Long userId,String username){
        return Jwts.builder()
                // JWT 头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 设置分类；设置过期时间 一个当前时间，一个加上设置的过期时间常量
                .setSubject("user-login")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                // 设置 token 主体信息，用于存储用户信息。
                .claim("userId", userId)
                .claim("username", username)
                // 设置加密算法
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 根据传入的 token 判断 token 是否存在与有效
     * @param jwtToken: 传入的 token
     * @return: 布尔值，代表 token 是否有效
     */
    public static boolean checkToken(String jwtToken){
        if (StringUtils.isEmpty(jwtToken)){
            return false;
        }
        try{
            // 解析 JWT 并获取 Claims 对象
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken);

            // 检查 JWT 是否已过期
            Date expiration = claimsJws.getBody().getExpiration();
            return !new Date().after(expiration);

            // 如果没有抛出异常并且未过期，则认为 token 有效
        }catch (ExpiredJwtException e) {
            // 如果是过期异常，单独处理，明确表示 token 已过期
            return false;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // 其他导致JWT无效的异常
            log.info(e.getMessage());
            return false;
        }
    }

    /**
     * 根据 token 获取用户 id
     * @param token: 传入的 token
     */
    public static Long getUserIdByJwtToken(String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (body.get("userId") instanceof Integer) ? ((Integer) body.get("userId")).longValue() : (Long) body.get("userId");
    }

    /**
     * 根据 token 获取用户名
     * @param token: 传入的 token
     */
    public static String getUsernameByJwtToken(String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (String) body.get("username");
    }

}
