package com.sjf.service.auth.impl;

import com.sjf.common.Result;
import com.sjf.component.AuthUser;
import com.sjf.constant.HttpCode;
import com.sjf.entity.admin.User;
import com.sjf.exception.CustomException;
import com.sjf.service.admin.MenuService;
import com.sjf.service.admin.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 自定义实现类，实现 security 中的 UserDetailsService 接口
 * @Author: SJF
 * @Date: 2024/1/14
 **/
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username).getData();
        // 判断用户是否存在。
        if(user == null){
            throw new CustomException(HttpCode.AUTHENTICATION_FAILURE,"用户名对应的用户不存在");
        }
        // 判断用户是否被禁用。
        if(user.getStatus() == 1){
            throw new CustomException(HttpCode.AUTHENTICATION_FAILURE,"用户已被禁用");
        }
        // 获取用户对应的权限信息。
        Result<List<String>> permByUserId = menuService.getPermByUserId(user.getId());
        if(permByUserId.getCode() == HttpCode.SUCCESS){
            List<String> permList = permByUserId.getData();
            // 封装权限信息为 security 指定的格式。
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String item : permList) {
                if(item != null){
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(item);
                    authorities.add(authority);
                }
            }
            return new AuthUser(user,authorities);
        }
        // 失败时返回空的权限集合。
        return new AuthUser(user, Collections.emptyList());
    }
}
