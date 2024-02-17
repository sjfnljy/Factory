package com.sjf.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.dto.admin.MenuDto;
import com.sjf.entity.admin.Menu;
import com.sjf.vo.admin.RoleAssignMenuVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 菜单 Service 接口
 * @Author: SJF
 * @Date: 2024/1/12 18:29
 */
public interface MenuService extends IService<Menu> {
    /**
     * 获取所有的菜单的详细信息
     * @return: 查询到的菜单信息
     */
    Result<List<Menu>> getAllMenu();

    /**
     * 根据菜单 id 查询菜单详细信息
     * @param menuId: 菜单 id
     * @return: 查询到的菜单信息
     */
    Result<Menu> getMenuByMenuId(Long menuId);

    /**
     * 根据用户 id 获取用户菜单
     * @param userId:用户 id
     * @return: 用户菜单
     */
    Result<List<MenuDto>> getRouterByUserId(Long userId);

    /**
     * 根据用户 id 获取用户按钮权限
     * @param userId: 用户 id
     * @return: 用户按钮权限
     */
    Result<List<String>> getPermByUserId(Long userId);

    /**
     * 新增系统菜单
     * @param menu: 传输的菜单对象
     * @return: 新增的结果信息
     */
    Result<Object> saveMenu(Menu menu);

    /**
     * 修改系统菜单
     * @param menu: 传输的修改对象
     * @return: 修改的结果信息
     */
    Result<Object> updateMenu(Menu menu);

    /**
     * 根据菜单 id 删除对应的菜单
     * @param menuId: 菜单 id
     * @return: 删除的结果信息
     */
    Result<Object> deleteMenu(Long menuId);

    /**
     * 根据角色 id 获取菜单集合
     * @param roleId: 角色 id
     * @return: 菜单列表
     */
    Result<Map<String , Object>> findMenuByRoleId(Long roleId);

    /**
     * 给角色分配菜单
     * @param roleAssignMenuVo: 角色分配菜单对象
     * @return: 结果信息
     */
    Result<Object> assignMenu(RoleAssignMenuVo roleAssignMenuVo);
}
