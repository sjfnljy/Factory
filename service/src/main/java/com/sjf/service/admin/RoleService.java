package com.sjf.service.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.admin.Role;
import com.sjf.vo.admin.RoleQueryVo;
import com.sjf.vo.admin.UserAssignRoleVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 角色 Service 接口
 * @Author: SJF
 * @Date: 2023/1/7
 **/
public interface RoleService extends IService<Role> {

    /** 查询获得所有的角色信息
     * @return: 返回的角色信息列表
     */
    List<Role> getAllRoles();


    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 分页的页码
     * @param size: 分页每页的数据量
     * @param roleQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<Role>> conditionQuery(Integer current, Integer size, RoleQueryVo roleQueryVo);

    /**
     * 根据传输的角色 id 查询角色的详细信息
     * @param roleId: 传入的查询的角色 id
     * @return: 查询的结果信息
     */
    Result<Role> getRoleByRoleId(Long roleId);

    /**
     * 校验新增的角色对象并完成新增
     * @param role: 传入的新增的角色对象
     * @return: 校验及新增的结果信息
     */
    Result<Object> saveRole(Role role);

    /**
     * 校验修改的角色对象并完成修改
     * @param role: 传入的修改的角色对象
     * @return: 校验及修改的结果信息
     */
    Result<Object> updateRole(Role role);

    /**
     * 根据传输的角色 id 删除对象
     * @param roleId: 传入的删除的角色 id
     * @return: 校验及删除的结果信息
     */
    Result<Object> deleteRole(Long roleId);

    /**
     * 根据传入的用户 id 获取用户已分配的角色信息与全部的角色信息
     * @param userId: 用户 id
     * @return: 用户已分配的角色与全部的角色信息
     */
    Result<Map<String, Object>> findRoleByUserId(Long userId);

    /**
     * 为用户分配对应的角色
     * @param userAssignRoleVo: 传输的分配条件对象
     * @return: 分配的结果信息
     */
    Result<Object> assignRole(UserAssignRoleVo userAssignRoleVo);
}
