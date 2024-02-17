package com.sjf.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.admin.Menu;
import com.sjf.entity.admin.RoleAndMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 系统角色和菜单中间表 Mapper 接口
 * @Author: SJF
 * @Date: 2023/1/8
 **/

@Repository
public interface RoleAndMenuMapper extends BaseMapper<RoleAndMenu> {
    List<Long> getMenuIdsByRoleId(@Param("role_id") Long roleId);

    List<Menu> getMenusByRoleId(@Param("role_id") Long roleId);

    void insertByIds(RoleAndMenu roleAndMenu);

    void deleteRoleMenuByRoleId(@Param("role_id") Long roleId);

    List<Long> getRoleIdsByMenuId(@Param("menu_id") Long menuId);
}
