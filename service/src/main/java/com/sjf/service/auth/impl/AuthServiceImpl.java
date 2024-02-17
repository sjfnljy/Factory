package com.sjf.service.auth.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.constant.RedisPrefix;
import com.sjf.dto.admin.MenuDto;
import com.sjf.dto.auth.LoginDto;
import com.sjf.dto.auth.ValidateCodeDto;
import com.sjf.entity.admin.User;
import com.sjf.mapper.admin.UserAndRoleMapper;
import com.sjf.mapper.admin.UserMapper;
import com.sjf.service.admin.MenuService;
import com.sjf.service.auth.AuthService;
import com.sjf.utils.RedisUtil;
import com.sjf.vo.auth.LoginVo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import com.sjf.utils.JWTUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @Description: 登录认证 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/13 12:17
 */

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private UserAndRoleMapper userAndRoleMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Result<LoginDto> tryLogin(LoginVo loginVo) {
        if(loginVo == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"未传输登录的用户信息");
        }
        // 校验传入的验证码。
        String captcha = loginVo.getCaptcha();
        String codeKey = loginVo.getCodeKey();
        String redisCode = (String) redisUtil.get(RedisPrefix.USER_VALIDATE_CODE + codeKey);
        if(StrUtil.isBlank(redisCode) || !StrUtil.equalsIgnoreCase(redisCode, captcha)){
            return Result.build(HttpCode.AUTHENTICATION_FAILURE,"验证码不正确");
        }

        // 验证通过则删除验证码。
       redisUtil.del(RedisPrefix.USER_VALIDATE_CODE + codeKey);

        // 获取传输的用户信息并校验用户名、密码是否合法。
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        if(StrUtil.isBlank(username)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"用户名不能为空");
        }
        if (StrUtil.isBlank(password)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"用户密码不能为空");
        }

        // 校验该用户名和密码对应的对象是否存在。
        User user = userMapper.getUserByUsername(username);
        if (user == null){
            return Result.build(HttpCode.AUTHENTICATION_FAILURE,"用户不存在");
        } else {
            if(!Objects.equals(user.getPassword(), SecureUtil.md5(password))){
                return Result.build(HttpCode.AUTHENTICATION_FAILURE,"密码不正确");
            }
            if(user.getStatus() == 1){
                return Result.build(HttpCode.AUTHORIZATION_FAILURE,"用户已被禁用，无法登录");
            }
        }

        // 校验通过则生成 token，存入缓存中.
        String token = JWTUtil.createJwtToken(user.getId(), username);
        redisUtil.set(RedisPrefix.LOGIN_PREFIX + user.getId(),token, 60L);

        // 将用户权限信息存入缓存。
        List<String> permList = menuService.getPermByUserId(user.getId()).getData();
        if(CollUtil.isNotEmpty(permList)){
            // 封装权限信息为 security 指定的格式。
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String perm : permList) {
                if(perm != null){
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(perm);
                    authorities.add(authority);
                }
            }
            redisUtil.set(RedisPrefix.USER_AUTHORITY + user.getId(), JSON.toJSONString(authorities),120L);
        }

        // 生成返回的对象给前端。
        LoginDto loginDto = new LoginDto();
        loginDto.setToken(token);
        loginDto.setRefresh_token("true");
        return Result.build(loginDto,HttpCode.SUCCESS,"登陆成功");
    }

    @Override
    public Result<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        // 获取请求体中的 token 信息并解析得到用户 id。
        Long userId = JWTUtil.getUserIdByJwtToken(request.getHeader("token"));
        User user = userMapper.getUserByUserId(userId);
        if (user == null){
            return Result.build(HttpCode.AUTHENTICATION_FAILURE,"用户不存在");
        }

        // 获取用户路菜单权限并返回。
        List<String> permList = menuService.getPermByUserId(userId).getData();

        // 封装用户信息。
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("username", user.getUsername());
        userInfoMap.put("role", userAndRoleMapper.getRoleByUserId(userId));
        userInfoMap.put("avatar", user.getHeadUrl());
        userInfoMap.put("perms", permList);
        return Result.ok(userInfoMap);
    }

    @Override
    public Result<ValidateCodeDto> generateValidateCode() {
        // 获取验证码:包含 4 位验证码值以及图片的 base64 编码。
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 20);
        String codeValue = circleCaptcha.getCode();
        String imageBase64 = circleCaptcha.getImageBase64();

        // 把验证码存储到 redis 里面并设置过期时间 5 分钟。
        String key = IdUtil.fastSimpleUUID();
        Boolean set = redisUtil.set(RedisPrefix.USER_VALIDATE_CODE + key, codeValue, 5L);

        //封装返回的 ValidateCodeDto 对象。
        ValidateCodeDto validateCodeDto = new ValidateCodeDto();
        validateCodeDto.setCodeKey(key);
        validateCodeDto.setCodeValue("data:image/png;base64," + imageBase64);
        return Result.ok(validateCodeDto);
    }

    @Override
    public Result<Object> logout(String token) {
        // 获取 token 中的用户 id。
        Long userId = JWTUtil.getUserIdByJwtToken(token);
        // 删除 redis 中的用户 token。
        redisUtil.del(RedisPrefix.LOGIN_PREFIX + userId);
        return Result.build(HttpCode.SUCCESS,"用户注销成功");
    }

    @Override
    public Result<List<MenuDto>> getRouters(String token) {
        // 获取 token 中的用户 id。
        Long userId = JWTUtil.getUserIdByJwtToken(token);
        return Result.build(menuService.getRouterByUserId(userId).getData(), HttpCode.SUCCESS, "获取成功");
    }
}
