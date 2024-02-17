package com.sjf.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.admin.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 用户 Mapper 接口
 * @Author: SJF
 * @Date: 2023/1/8
 **/

@Repository
public interface UserMapper extends BaseMapper<User> {
    List<User> getAllUsers();

    User getUserByUserId(@Param("user_id") Long userId);

    void insertUser(User user);

    void updateUser(User user);

    void deleteUserById(@Param("user_id") Long userId);

    User getUserByUsername(@Param("username") String username);

    void updateUserPassword(@Param("user_id") Long userId, @Param("password") String password);

    void updateUserStatus(@Param("user_id") Long userId, @Param("status") Integer status);
}
