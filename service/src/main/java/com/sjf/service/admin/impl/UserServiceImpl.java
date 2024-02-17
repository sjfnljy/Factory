package com.sjf.service.admin.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.admin.User;
import com.sjf.entity.admin.UserAndRole;
import com.sjf.mapper.admin.UserAndRoleMapper;
import com.sjf.mapper.admin.UserMapper;
import com.sjf.service.admin.UserService;
import com.sjf.vo.admin.UserQueryVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 系统用户 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/9 22:38
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAndRoleMapper userAndRoleMapper;

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public Result<Page<User>> conditionQuery(Integer current, Integer size, UserQueryVo userQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(userQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        }

        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if(current <= 0 || size <= 0){
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }

        // 获取传输的查询条件。
        String username = userQueryVo.getUsername();
        String name = userQueryVo.getName();
        String phone = userQueryVo.getPhone();
        String timeBegin = userQueryVo.getCreateTimeBegin();
        String timeEnd = userQueryVo.getCreateTimeEnd();

        // 执行查询细节：姓名和用户名支持模糊查询，手机号码相等查询，起始时间和结束时间范围查询。
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(name),User::getName,name)
                .like(StrUtil.isNotEmpty(username),User::getUsername,username)
                .eq(StrUtil.isNotEmpty(phone),User::getPhone,phone)
                .ge(StrUtil.isNotEmpty(timeBegin),User::getCreateTime,timeBegin)
                .le(StrUtil.isNotEmpty(timeEnd),User::getCreateTime,timeEnd);

        // 封装分页信息并返回结果.
        Page<User> page = new Page<>(current, size);
        Page<User> userPage = userMapper.selectPage(page, wrapper);
        if(userPage == null){
            return Result.build(HttpCode.QUERY_FAILURE,"条件查询结果不存在");
        }

        // 获取用户对应的角色信息并对手机号码进行脱敏处理。
        List<User> userList = userPage.getRecords();
        if(CollUtil.isNotEmpty(userList)) {
            for (User auser : userList){
                if(auser != null){
                    Long roleId = userAndRoleMapper.getRoleIdByUserId(auser.getId());
                    auser.setQueryRoleId(roleId);
                    auser.setPhone(DesensitizedUtil.mobilePhone(auser.getPhone()));
                }
            }
        }
        userPage.setRecords(userList);
        // 查询成功返回分页结果信息。
        return Result.ok(userPage);
    }

    @Override
    public Result<User> getUserByUserId(Long userId) {
        if (StrUtil.isBlank(String.valueOf(userId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询的用户 id 不能为空");
        }

        // 校验传入的用户 id 对应的对象是否存在。
        User user = userMapper.getUserByUserId(userId);
        if(user == null){
            // 传入的用户 id 对应的对象不存在。
            return Result.build(HttpCode.SUCCESS,"不存在该用户 id 对应的用户对象");
        }

        // 查询成功则返回带有角色信息且进行手机号码脱敏的用户详细信息。
        Long roleId = userAndRoleMapper.getRoleIdByUserId(user.getId());
        user.setQueryRoleId(roleId);
        user.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
        return Result.ok(user);
    }

    @Override
    public Result<User> getUserByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询的用户名不能为空");
        }
        User user = userMapper.getUserByUsername(username);
        return Result.ok(user);

    }

    @Override
    public Result<Object> saveUser(User user) {
        if(user == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的用户对象不存在");
        }

        // 校验传输的用户名。
        String username = user.getUsername();
        if(StrUtil.isBlank(username)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的用户名不能为空");
        }

        // 校验传输的密码。
        String password = user.getPassword();
        if(StrUtil.isBlank(password)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的密码不能为空");
        }else{
            // 已传输密码则进行加密。
            String encryptedPassword = SecureUtil.md5(password);
            user.setPassword(encryptedPassword);
        }

        // 校验传输的用户姓名。
        String name = user.getName();
        if (StrUtil.isBlank(name)) {
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的用户姓名不能为空");
        }

        // 校验传输的手机号。
        String phone = user.getPhone();
        if (StrUtil.isBlank(phone)) {
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的用户手机号码不能为空");
        }else if(!Validator.isMobile(phone)){
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的用户手机号码不合法");
        }

        // 校验头像的 URL 地址是否可用。
        if (!urlExists(user.getHeadUrl())){
            return Result.build(HttpCode.INVALID_PARAMETER,"头像的 URL 地址不合法");
        }

        // 校验用户禁用标志，新用户必须为可用状态。
        Integer status = user.getStatus();
        if (status != 0) {
            return Result.build(HttpCode.INVALID_PARAMETER,"新增用户的状态必须为可用");
        }

        // 校验全部通过则插入数据库中。
        userMapper.insertUser(user);

        // 如果传入了 createRoleId 则代表创建用户的同时分配角色。
        Long createRoleId = user.getCreateRoleId();
        if(createRoleId!= null){
            UserAndRole userAndRole = new UserAndRole(user.getId(), createRoleId);
            userAndRoleMapper.insertUserRole(userAndRole);
        }
        return Result.build(HttpCode.SUCCESS,"新增用户成功");
    }

    @Override
    public Result<Object> updateUser(User user) {
        if(user == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的用户对象不存在");
        }

        // 获取传输的用户信息并校验传输的用户对象 id 是否存在。
        Long userId = user.getId();
        if (StrUtil.isBlank(String.valueOf(userId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的修改的用户 id 不能为空");
        } else {
            // id 存在则校验数据库中是否存在对应的角色对象。
            User auser = userMapper.getUserByUserId(userId);
            if (auser == null){
                return Result.build(HttpCode.MODIFICATION_FAILURE,"不存在该用户 id 对应的用户，无法进行修改");
            }
        }

        // 校验通过则对用户进行修改。
        userMapper.updateUser(user);
        return Result.build(HttpCode.SUCCESS,"修改用户成功");
    }

    @Override
    public Result<Object> updateUserPassword(Long userId, String password) {
        // 校验传入的参数。
        if (StrUtil.isBlank(String.valueOf(userId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的修改的用户 id 不能为空");
        } else if (StrUtil.isBlank(password)){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的修改的用户密码不能为空");
        }

        // 校验数据库中是否存在传输的用户 id 对应的用户。
        User quiredUser = userMapper.getUserByUserId(userId);
        if (quiredUser == null){
            return Result.build(HttpCode.MODIFICATION_FAILURE,"不存在该用户 id 对应的用户，无法进行修改");
        }

        // 校验要修改的用户密码是否和用户当前值不一样。
        if (Objects.equals(SecureUtil.md5(password), quiredUser.getPassword())){
            return Result.build(HttpCode.MODIFICATION_FAILURE,"修改的用户密码与当前值一致");
        }

        // 校验通过则进行修改。
        userMapper.updateUserPassword(userId, SecureUtil.md5(password));
        return Result.build(HttpCode.SUCCESS,"修改用户密码成功");
    }

    @Override
    public Result<Object> updateUserStatus(Long userId, Integer status) {
        // 校验传入的参数。
        if (StrUtil.isBlank(String.valueOf(userId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的修改的用户 id 不能为空");
        } else if (StrUtil.isBlank(String.valueOf(status))){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的修改的用户状态不能为空");
        }else if(status != 0 && status != 1){
            return Result.build(HttpCode.INVALID_PARAMETER, "传入的修改的用户状态不合法");
        }

        // 校验数据库中是否存在传输的用户 id 对应的用户。
        User quiredUser = userMapper.getUserByUserId(userId);
        if (quiredUser == null){
            return Result.build(HttpCode.MODIFICATION_FAILURE,"不存在该用户 id 对应的用户，无法进行修改");
        }

        // 校验要修改的用户状态值是否和用户当前值不一样。
        if (Objects.equals(status, quiredUser.getStatus())){
            return Result.build(HttpCode.MODIFICATION_FAILURE,"修改的用户状态值与当前值一致");
        }

        // 校验通过则进行修改。
        userMapper.updateUserStatus(userId, status);
        return Result.build(HttpCode.SUCCESS,"修改用户状态成功");
    }

    @Override
    public Result<Object> deleteUser(Long userId) {
        if(StrUtil.isBlank(String.valueOf(userId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的删除的用户 id 不能为空");
        }

        // 校验传入的用户 id 对应的对象是否存在。
        User user = userMapper.getUserByUserId(userId);
        if (user == null){
            return Result.build(HttpCode.DELETION_FAILURE,"不存在该用户 id 对应的用户");
        }

        // 校验通过则删除用户信息与用户角色中间表关联记录。
        userMapper.deleteUserById(userId);
        userAndRoleMapper.deleteUserRoleByUserId(userId);
        return Result.build(HttpCode.SUCCESS,"删除用户成功");
    }

    public boolean urlExists(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int responseCode = huc.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (Exception e) {
            return false;
        }
    }
}
