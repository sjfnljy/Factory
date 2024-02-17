package com.sjf.controller.admin;

import com.sjf.common.Result;
import com.sjf.dto.admin.MenuDto;
import com.sjf.entity.admin.Menu;
import com.sjf.service.admin.MenuService;
import com.sjf.vo.admin.RoleAssignMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 菜单请求处理器
 * @Author: SJF
 * @Date: 2024/1/12 18:25
 */

@Api(tags = "系统菜单管理接口")
@RestController
@RequestMapping("/factory/admin/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @ApiOperation(value = "获取所有菜单")
    @GetMapping("/all")
    public Result<List<Menu>> getAllMenu(){
        return menuService.getAllMenu();
    }

    @ApiOperation("根据菜单 id 查询菜单详细信息")
    @GetMapping("/get/{menuId}")
    public Result<Menu> getMenuById(@PathVariable("menuId") Long menuId){
        return menuService.getMenuByMenuId(menuId);
    }

    @ApiOperation("根据用户 id 查询用户路由信息")
    @GetMapping("/get/router/{userId}")
    public Result<List<MenuDto>> getRouterByUserId(@PathVariable("userId") Long userId){
        return menuService.getRouterByUserId(userId);
    }

    @ApiOperation("根据用户 id 查询用户权限标识信息")
    @GetMapping("/get/perm/{userId}")
    public Result<List<String>> getPermByUserId(@PathVariable("userId") Long userId){
        return menuService.getPermByUserId(userId);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("/save")
    public Result<Object> saveMenu(@RequestBody Menu menu){
        return menuService.saveMenu(menu);
    }

    @ApiOperation("修改系统菜单")
    @PutMapping("/update")
    public Result<Object> updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @ApiOperation("根据菜单 id 删除菜单")
    @DeleteMapping("/delete/{menuId}")
    public Result<Object> deleteMenu(@PathVariable("menuId") Long menuId){
        return menuService.deleteMenu(menuId);
    }

    @ApiOperation(value = "根据角色 id 获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result<Map<String , Object>> toAssign(@PathVariable("roleId") Long roleId) {
        return menuService.findMenuByRoleId(roleId);
    }

    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result<Object> doAssign(@RequestBody RoleAssignMenuVo roleAssignMenuVo) {
        return menuService.assignMenu(roleAssignMenuVo);
    }
}
