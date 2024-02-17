package com.sjf.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.admin.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 菜单 Mapper 接口
 * @Author: SJF
 * @Date: 2023/1/8
 **/

@Repository
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getAllMenu();

    List<Menu> getMenuListByUserId(@Param("user_id") Long userId);

    void insertMenu(Menu menu);

    Menu getMenuById(@Param("menu_id") Long menuId);

    void updateMenu(Menu menu);

    List<Long> getChildrenId(@Param("menu_id") Long menuId);

    void deleteMenu(@Param("menu_id") Long menuId);

    List<Menu> getAllRouter();

    List<Menu> getRouterListByUserId(@Param("user_id") Long userId);
}
