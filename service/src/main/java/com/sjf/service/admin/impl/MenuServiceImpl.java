package com.sjf.service.admin.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.dto.admin.MenuDto;
import com.sjf.entity.admin.Menu;
import com.sjf.entity.admin.RoleAndMenu;
import com.sjf.mapper.admin.MenuMapper;
import com.sjf.mapper.admin.RoleAndMenuMapper;
import com.sjf.service.admin.MenuService;
import com.sjf.vo.admin.RoleAssignMenuVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 菜单 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/12 18:30
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleAndMenuMapper roleAndMenuMapper;

    @Override
    public Result<List<Menu>> getAllMenu() {
        // 先查询所有的可用菜单。
        List<Menu> menuList = menuMapper.getAllMenu();
        if(CollUtil.isEmpty(menuList)){
            return Result.build(HttpCode.QUERY_FAILURE, "未查询到任何菜单信息");
        }
        return Result.ok(buildTreeMenu(menuList));
    }

    @Override
    public Result<Menu> getMenuByMenuId(Long menuId) {
        if (StrUtil.isBlank(String.valueOf(menuId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询的菜单 id 不能为空");
        }

        // 先查询所有的可用菜单和当前菜单 id 对应的菜单。
        List<Menu> menuList = menuMapper.getAllMenu();
        if(CollUtil.isEmpty(menuList)){
            return Result.build(HttpCode.QUERY_FAILURE, "未查询到任何菜单信息");
        }
        Menu menu = menuList.stream().filter(item -> item.getId().equals(menuId)).findFirst().orElse(null);
        if(menu == null){
            return Result.build(HttpCode.QUERY_FAILURE, "未查询到菜单 id 对应的菜单信息");
        }

        // 返回带有子菜单信息的菜单详细信息。
        return Result.ok(findChildren(menu, menuList));

    }

    @Override
    public Result<List<MenuDto>> getRouterByUserId(Long userId) {
        if (StrUtil.isBlank(String.valueOf(userId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询的用户 id 不能为空");
        }
        List<Menu> menuList;

        // 超级管理员直接拥有所有可用路由。
        if(userId == 1){
            menuList = menuMapper.getAllRouter();
        }else {
            menuList = menuMapper.getRouterListByUserId(userId);
        }

        // 构建树形结构与路由。
        List<Menu> treeMenuList = buildTreeMenu(menuList);
        return Result.ok(toMenuDto(treeMenuList));
    }

    private List<MenuDto> toMenuDto(List<Menu> treeMenuList) {
        List<MenuDto> sysMenuDtoList = new ArrayList<>();
        for (Menu aMenu : treeMenuList) {
            MenuDto sysMenuDto = new MenuDto();
            sysMenuDto.setTitle(aMenu.getName());
            sysMenuDto.setName(aMenu.getComponent());
            List<Menu> children = aMenu.getChildren();
            if (CollUtil.isNotEmpty(children)) {
                sysMenuDto.setChildren(toMenuDto(children));
            }
            sysMenuDtoList.add(sysMenuDto);
        }
        return sysMenuDtoList;
    }

    @Override
    public Result<List<String>> getPermByUserId(Long userId) {
        if (StrUtil.isBlank(String.valueOf(userId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询的用户 id 不能为空");
        }

        List<Menu> menuList;
        // 超级管理员直接拥有所有可用按钮权限。
        if (userId == 1) {
            menuList = menuMapper.getAllMenu();
        }else{
            menuList = menuMapper.getMenuListByUserId(userId);
        }

        // 从权限集合中获取所有的按钮权限。
        List<String> permList = menuList.stream()
                // 先筛选得到所有的按钮。
                .filter(item -> item.getType() == 2)
                // 获取每个按钮对应的 perm 标识。
                .map(Menu::getPerms)
                .collect(Collectors.toList());
        return Result.ok(permList);
    }

    @Override
    public Result<Object> saveMenu(Menu menu) {
        if(menu == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的菜单对象不存在");
        }

        // 校验传输的菜单名称和上级菜单 id，不能为空。
        if (StrUtil.isBlank(menu.getName())) {
            return Result.build(HttpCode.INVALID_PARAMETER, "菜单名称不能为空");
        }
        if (StrUtil.isBlank(String.valueOf(menu.getParentId()))) {
            return Result.build(HttpCode.INVALID_PARAMETER, "需要为菜单指定上级菜单");
        }

        // 校验菜单类型、路由地址、组件和功能权限标识是否合理。
        Integer type = menu.getType();
        if (type != 0 && type != 1 && type != 2) {
            return Result.build(HttpCode.INVALID_PARAMETER, "菜单类型不合法");
        } else if (type == 1) {
            // 当前菜单类型是路由，则路由地址必须同时存在。
            if (StrUtil.isBlank(menu.getComponent())) {
                return Result.build(HttpCode.INVALID_PARAMETER, "路由的路由地址必须存在");
            }
        } else if(type == 2) {
            // 当前菜单类型是按钮，则权限功能标识不能为空。
            if (StrUtil.isBlank(menu.getPerms())) {
                return Result.build(HttpCode.INVALID_PARAMETER, "按钮的功能权限标识不能为空");
            }
        }

        // 校验菜单状态，创建时必须为可用状态。
        if (StrUtil.isBlank(String.valueOf(menu.getStatus())) || menu.getStatus() != 0) {
            return Result.build(HttpCode.INVALID_PARAMETER, "创建菜单时菜单状态必须为可用");
        }

        // 校验通过则插入数据库中。
        menuMapper.insertMenu(menu);
        return Result.build(HttpCode.SUCCESS, "新增菜单成功");
    }

    @Override
    public Result<Object> updateMenu(Menu menu) {
        if(menu == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的菜单对象不存在");
        }

        // 修改时必须传入菜单 id。
        Long menuId = menu.getId();
        if (StrUtil.isBlank(String.valueOf(menuId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的修改的菜单 id 不能为空");
        } else {
            // id 存在则校验数据库中是否存在对应的菜单对象。
            Menu quiredMenu = menuMapper.getMenuById(menuId);
            if (quiredMenu == null) {
                return Result.build(HttpCode.QUERY_FAILURE, "未查询到菜单 id 对应的菜单信息");
            }
        }

        // 校验通过则进行修改。
        menuMapper.updateMenu(menu);
        return Result.build(HttpCode.SUCCESS, "修改菜单成功");
    }

    @Override
    public Result<Object> deleteMenu(Long menuId) {
        if(StrUtil.isBlank(String.valueOf(menuId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的删除的菜单 id 不能为空");
        }

        // 校验数据库中是否存在该菜单 id 对应的菜单。
        Menu quiredMenu = menuMapper.getMenuById(menuId);
        if (quiredMenu == null) {
            return Result.build(HttpCode.QUERY_FAILURE, "未查询到菜单 id 对应的菜单信息");
        }

        // 校验该菜单是否为其他菜单的上级菜单，若是则不能删除。
        List<Long> childrenIdList = menuMapper.getChildrenId(menuId);
        if (CollUtil.isNotEmpty(childrenIdList)){
            return Result.build(HttpCode.DELETION_FAILURE, "该菜单是其他可用菜单的上级菜单，不可删除");
        }

        // 校验是否还有角色在使用该菜单，若有则不能删除。
        List<Long> roleIdList = roleAndMenuMapper.getRoleIdsByMenuId(menuId);
        if (CollUtil.isNotEmpty(roleIdList)){
            return Result.build(HttpCode.DELETION_FAILURE, "仍有角色在使用菜单，无法删除菜单");
        }

        // 校验通过则进行删除。
        menuMapper.deleteMenu(menuId);
        return Result.build(HttpCode.SUCCESS, "删除菜单成功");
    }

    @Override
    public Result<Map<String, Object>> findMenuByRoleId(Long roleId) {
        // 获取全部的权限列表。
        List<Menu> menuList = getAllMenu().getData();
        if (CollUtil.isEmpty(menuList)){
            return Result.build(HttpCode.QUERY_FAILURE, "可使用的权限列表为空");
        }

        // 获取角色 id 对应全部权限 id 列表.

        List<Long> roleMenuIds = roleId == 1 ? menuList.stream().map(Menu::getId).collect(Collectors.toList())
                : roleAndMenuMapper.getMenuIdsByRoleId(roleId);

        //封装返回数据
        Map<String, Object> result = new HashMap<>() ;
        result.put("sysMenuList" , menuList) ;
        result.put("roleMenuIds" , roleMenuIds) ;
        return Result.ok(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> assignMenu(RoleAssignMenuVo roleAssignMenuVo) {
        if (roleAssignMenuVo == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的角色分配菜单对象不存在");
        }

        // 先删除中间表记录。
        Long roleId = roleAssignMenuVo.getRoleId();
        roleAndMenuMapper.deleteRoleMenuByRoleId(roleId);

        // 再插入中间表记录。
        for (Long menuId : roleAssignMenuVo.getMenuIdList()) {
            RoleAndMenu roleAndMenu = new RoleAndMenu(roleId, menuId);
            roleAndMenuMapper.insertByIds(roleAndMenu);
        }
        return Result.build(HttpCode.SUCCESS, "分配菜单成功");
    }


    public List<Menu> buildTreeMenu(List<Menu> menuList) {
        List<Menu> treeMenuList = new ArrayList<>();
        for (Menu amenu : menuList) {
            if (amenu != null) {
                // 遍历整个集合找到所有可以构建树形结构的根节点。
                if (amenu.getParentId() == 0) {
                    treeMenuList.add(findChildren(amenu, menuList));
                }
            }
        }
        return treeMenuList;
    }


    private Menu findChildren(Menu menu, List<Menu> menuList) {
        menu.setChildren(new ArrayList<>());
        // 遍历整个集合查询当前根节点的所有子节点。
        for (Menu amenu : menuList) {
            if (amenu != null) {
                // 根节点的 id 恰好是集合节点的父 id。
                if (menu.getId().equals(amenu.getParentId())) {
                    // 第一个节点进入时，根节点的子节点集合为空。
                    if (menu.getChildren() == null) {
                        List<Menu> menuChildrenList = new ArrayList<>();
                        menu.setChildren(menuChildrenList);
                    }
                    // 递归构建子节点的子节点。
                    menu.getChildren().add(findChildren(amenu, menuList));
                }
            }
        }
        return menu;
    }


}
