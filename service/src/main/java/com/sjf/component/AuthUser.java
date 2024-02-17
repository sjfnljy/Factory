package com.sjf.component;

import com.sjf.entity.admin.User;
import org.springframework.security.core.GrantedAuthority;


import java.util.Collection;

/**
 * @Description: 自定义用户认证用户实体类
 * @Author: SJF
 * @Date: 2024/1/14
 **/
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthUser(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }

    public User getSystemUser() {
        return user;
    }
}
