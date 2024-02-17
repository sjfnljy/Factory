package com.sjf.controller.admin;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.admin.User;
import com.sjf.service.admin.UserService;
import com.sjf.vo.admin.UserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 用户请求处理器
 * @Author: SJF
 * @Date: 2024/1/9 22:36
 */
@Api(tags = "系统用户管理接口")
@RestController
@RequestMapping("/factory/admin/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("查询所有用户")
    @GetMapping("/all")
    public Result<List<User>> getAllUser(){
        List<User> userList = userService.getAllUsers();
        if(CollUtil.isEmpty(userList)){
            return Result.build(HttpCode.QUERY_FAILURE,"查询结果不存在");
        }
        return Result.ok(userList);
    }

    @ApiOperation("条件查询用户信息并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<User>> conditionQuery(@PathVariable("current") Integer current,
                                             @PathVariable("size") Integer size,
                                             UserQueryVo userQueryVo){
        return userService.conditionQuery(current, size,userQueryVo);
    }

    @ApiOperation("根据用户 id 查询用户详细信息")
    @GetMapping("/get/{userId}")
    public Result<User> getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserByUserId(userId);
    }

    @ApiOperation("根据用户名查询用户详细信息")
    @GetMapping("/get/username/{username}")
    public Result<User> getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }

    @ApiOperation("新增系统用户")
    @PostMapping("/save")
    public Result<Object> saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @ApiOperation("修改系统用户信息")
    @PutMapping("/update")
    public Result<Object> updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @ApiOperation("修改系统用户密码")
    @PutMapping("/update/password/{userId}/{password}")
    public Result<Object> updateUserPassword(@PathVariable("userId") Long userId, @PathVariable("password") String password) {
        return userService.updateUserPassword(userId, password);
    }

    @ApiOperation("修改系统用户状态")
    @PutMapping("/update/status/{userId}/{status}")
    public Result<Object> updateUserStatus(@PathVariable("userId") Long userId, @PathVariable("status") Integer status){
        return userService.updateUserStatus(userId, status);
    }

    @ApiOperation("根据用户 id 删除用户")
    @DeleteMapping("/delete/{userId}")
    public Result<Object> deleteUser(@PathVariable("userId") Long userId){
        return userService.deleteUser(userId);
    }


}
