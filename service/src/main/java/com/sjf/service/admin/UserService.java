package com.sjf.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.admin.User;
import com.sjf.vo.admin.UserQueryVo;

import java.util.List;

/**
 * @Description: 用户 Service 接口
 * @Author: SJF
 * @Date: 2023/1/7
 **/

public interface UserService extends IService<User> {

    /**
     * 查询获得所有的用户信息
     * @return: 返回的用户信息列表
     */
    List<User> getAllUsers();

    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 分页的页码
     * @param size: 分页每页的数据量
     * @param userQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<User>> conditionQuery(Integer current, Integer size, UserQueryVo userQueryVo);

    /**
     * 根据传输的用户 id 查询用户的详细信息
     * @param userId: 传入的查询的用户 id
     * @return: 查询的结果信息
     */
    Result<User> getUserByUserId(Long userId);

    /**
     * 根据传输的用户名查询用户的详细信息
     * @param username: 传入的查询的用户名
     * @return: 查询的结果信息
     */
    Result<User> getUserByUsername(String username);

    /**
     * 校验新增的用户对象并完成新增
     * @param user: 传入的新增的用户对象
     * @return: 校验及新增的结果信息
     */
    Result<Object> saveUser(User user);

    /**
     * 校验修改的用户对象并完成修改
     * @param user: 传入的修改的用户对象
     * @return: 校验及修改的结果信息
     */
    Result<Object> updateUser(User user);

    /**
     * 根据传输的用户 id 更新用户的密码
     * @param userId: 用户 id
     * @param password: 更新后的密码
     * @return: 更新密码的结果信息
     */
    Result<Object> updateUserPassword(Long userId, String password);

    /**
     * 据传输的用户 id 更新用户的状态
     * @param userId: 用户 id
     * @param status: 更新后的状态
     * @return: 更新状态的结果信息
     */
    Result<Object> updateUserStatus(Long userId, Integer status);

    /**
     * 根据传输的用户 id 删除对象
     * @param userId: 传入的删除的用户 id
     * @return: 校验及删除的结果信息
     */
    Result<Object> deleteUser(Long userId);
}
