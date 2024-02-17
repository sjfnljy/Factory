package com.sjf.controller.admin;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.log.Log;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.constant.OperatorType;
import com.sjf.entity.admin.Role;
import com.sjf.service.admin.RoleService;
import com.sjf.vo.admin.RoleQueryVo;
import com.sjf.vo.admin.UserAssignRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 角色服务 Controller
 * @Author: SJF
 * @Date: 2024/1/8 19:01
 */
@Api(tags = "角色请求处理器")
@RestController
@RequestMapping("factory/admin/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation("查询所有角色")
    @GetMapping("/all")
    public Result<List<Role>> getAllRoles(){
        List<Role> roleList = roleService.getAllRoles();
        if(CollectionUtil.isEmpty(roleList)){
            return Result.build(HttpCode.QUERY_FAILURE,"角色查询结果不存在");
        }
        return Result.ok(roleList);
    }

    @ApiOperation("条件查询角色信息并分页返回")
    @Log(title = "角色条件查询", businessType = 0, operatorType = OperatorType.MANAGE)
    @GetMapping("/page/{current}/{size}")
    public Result<Page<Role>> conditionQuery(@PathVariable("current") Integer current,
                                             @PathVariable("size") Integer size,
                                             RoleQueryVo roleQueryVo){
        return roleService.conditionQuery(current, size,roleQueryVo);
    }

    @ApiOperation("根据角色 id 查询角色详细信息")
    @Log(title = "角色查询", businessType = 0, operatorType = OperatorType.MANAGE)
    @GetMapping("/get/{roleId}")
    public Result<Role> getRoleById(@PathVariable("roleId") Long roleId) {
        return roleService.getRoleByRoleId(roleId);
    }

    @ApiOperation("新增角色信息")
    @Log(title = "角色添加", businessType = 1, operatorType = OperatorType.MANAGE)
    @PostMapping("/save")
    public Result<Object> saveRole(@RequestBody Role role){
        return roleService.saveRole(role);
    }

    @ApiOperation("修改角色信息")
    @Log(title = "角色修改", businessType = 2, operatorType = OperatorType.MANAGE)
    @PutMapping("/update")
    public Result<Object> updateRole(@RequestBody Role role){
        return roleService.updateRole(role);
    }

    @ApiOperation("根据角色 id 删除角色")
    @Log(title = "角色删除", businessType = 3, operatorType = OperatorType.MANAGE)
    @DeleteMapping("/delete/{roleId}")
    public Result<Object> deleteRole(@PathVariable("roleId") Long roleId){
        return roleService.deleteRole(roleId);
    }

    @ApiOperation(value = "根据用户 id 获取角色数据")
    @Log(title = "查询用户已分配角色", businessType = 0, operatorType = OperatorType.MANAGE)
    @GetMapping("/get/roleOfUser/{userId}")
    public Result<Map<String, Object>> toAssign(@PathVariable Long userId) {
        return roleService.findRoleByUserId(userId);
    }

    @ApiOperation(value = "为用户分配对应的角色")
    @Log(title = "为用户分配角色", businessType = 1, operatorType = OperatorType.MANAGE)
    @PostMapping("/user/assign/role")
    public Result<Object> doAssign(@RequestBody UserAssignRoleVo userAssignRoleVo) {
        return roleService.assignRole(userAssignRoleVo);
    }
}
