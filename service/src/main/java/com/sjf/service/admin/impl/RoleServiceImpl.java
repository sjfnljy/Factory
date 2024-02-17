package com.sjf.service.admin.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.admin.Role;
import com.sjf.entity.admin.RoleAndMenu;
import com.sjf.entity.admin.UserAndRole;
import com.sjf.mapper.admin.RoleAndMenuMapper;
import com.sjf.mapper.admin.RoleMapper;
import com.sjf.mapper.admin.UserAndRoleMapper;
import com.sjf.service.admin.RoleService;
import com.sjf.vo.admin.RoleQueryVo;
import com.sjf.vo.admin.UserAssignRoleVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sjf.utils.PrefixGenerateUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 角色服务 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/8 18:59
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService  {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleAndMenuMapper roleAndMenuMapper;

    @Resource
    private UserAndRoleMapper userAndRoleMapper;

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.getAllRoles();
    }

    @Override
    public Result<Page<Role>> conditionQuery(Integer current, Integer size, RoleQueryVo roleQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(roleQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        }

        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if(current <= 0 || size <= 0){
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }

        // 获取传输的查询条件。
        String roleCode = roleQueryVo.getRoleCode();
        String roleName = roleQueryVo.getRoleName();
        Page<Role> page = new Page<>(current, size);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();

        // 角色名称模糊查询、角色编码准确匹配。
        wrapper.like(StrUtil.isNotEmpty(roleName),Role::getRoleName,roleName)
                .or()
                .eq(StrUtil.isNotEmpty(roleCode),Role::getRoleCode,roleCode);
        Page<Role> rolePage = roleMapper.selectPage(page, wrapper);

        // 分页返回结果为空时，代表查询成功但是没有数据。
        if(rolePage == null){
            return Result.build(HttpCode.SUCCESS,"条件查询结果不存在");
        }

        // 获取角色对应的菜单信息。
        List<Role> roleList = rolePage.getRecords();
        if(CollUtil.isNotEmpty(roleList)){
            for (Role arole : roleList) {
                if(arole != null){
                    List<Long> menuIdList = roleAndMenuMapper.getMenuIdsByRoleId(arole.getId());
                    arole.setQueryMenuIdList(menuIdList);
                }
            }
        }
        rolePage.setRecords(roleList);
        // 查询成功返回分页结果信息。
        return Result.ok(rolePage);
    }

    @Override
    public Result<Role> getRoleByRoleId(Long roleId) {
        if (StrUtil.isBlank(String.valueOf(roleId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询的角色 id 不能为空");
        }

        // 校验传入的角色 id 对应的对象是否存在。
        Role role = roleMapper.getRoleByRoleId(roleId);
        if(role == null){
            // 传入的角色 id 对应的对象不存在。
            return Result.build(HttpCode.SUCCESS,"不存在该角色 id 对应的角色对象");
        }

        // 查询成功则返回带有菜单信息的角色详细信息。
        List<Long> menuIdList = roleAndMenuMapper.getMenuIdsByRoleId(role.getId());
        role.setQueryMenuIdList(menuIdList);
        return Result.ok(role);
    }

    @Override
    public Result<Object> saveRole(Role role) {
        if(role == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的角色对象不存在");
        }

        // 校验传输的角色名称信息：
        String roleName = role.getRoleName();
        if(StrUtil.isBlank(roleName)){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "创建角色时未传入对应的角色名称");
        }else{
            Role queriedRole = roleMapper.getRoleByRoleName(roleName);
            if(queriedRole != null){
                return Result.build(HttpCode.ADD_FAILURE,"传入的角色名称对应的角色已存在");
            }
        }

        // 校验传输的角色编码信息：
        String roleCode = role.getRoleCode();
        if(StrUtil.isBlank(roleCode)){
            String generatedRoleCode = PrefixGenerateUtil.generateCode(roleName);
            role.setRoleCode(generatedRoleCode);
        }else{
            Role queriedRole = roleMapper.getRoleByRoleCode(roleCode);
            if(queriedRole != null){
                return Result.build(HttpCode.ADD_FAILURE,"传入的角色编码对应的角色已存在");
            }
        }

        // 校验通过则向数据库中插入角色对象。
        roleMapper.insertRole(role);

        // 如果传入了 createMenuIdList 则代表创建对象的同时分配菜单。
        List<Long> createMenuIdList = role.getCreateMenuIdList();
        if(CollUtil.isNotEmpty(createMenuIdList)){
            for (Long menuId : createMenuIdList) {
                if (StrUtil.isNotBlank(String.valueOf(menuId))) {
                    RoleAndMenu roleAndMenu = new RoleAndMenu(role.getId(), menuId);
                    roleAndMenuMapper.insertByIds(roleAndMenu);
                }
            }
        }
        return Result.build(HttpCode.SUCCESS,"新增角色成功");
    }

    @Override
    public Result<Object> updateRole(Role role) {
        if(role == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的角色对象不存在");
        }

        // 获取传输的角色信息并校验传输的角色对象 id 是否存在。
        Long roleId = role.getId();
        if (StrUtil.isBlank(String.valueOf(roleId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的修改的角色 id 不能为空");
        } else {
            // id 存在则校验数据库中是否存在对应的角色对象。
            Role quiredRole = roleMapper.getRoleByRoleId(roleId);
            if (quiredRole == null) {
                return Result.build(HttpCode.MODIFICATION_FAILURE,"不存在该角色 id 对应的角色，无法进行修改");
            }
        }

        // 校验通过则进行修改。
        roleMapper.updateRole(role);
        return Result.build(HttpCode.SUCCESS,"修改角色成功");
    }

    @Override
    public Result<Object> deleteRole(Long roleId) {
        if(StrUtil.isBlank(String.valueOf(roleId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的删除的角色 id 不能为空");
        }
        // 校验传入的角色 id 对应的对象是否存在。
        Role quiredRole = roleMapper.getRoleByRoleId(roleId);
        if(quiredRole == null){
            return Result.build(HttpCode.DELETION_FAILURE,"不存在该角色 id 对应的角色");
        }

        // 校验是否有用户还在使用角色，若有则不能删除。
        List<Integer> list = userAndRoleMapper.getUserIdByRoleId(roleId);
        if(CollUtil.isNotEmpty(list)){
            return Result.build(HttpCode.DELETION_FAILURE,"仍有用户在使用角色，无法删除角色");
        }

        // 校验通过则删除角色信息与角色菜单中间表关联记录。
        roleMapper.deleteRoleById(roleId);
        roleAndMenuMapper.deleteRoleMenuByRoleId(roleId);
        return Result.build(HttpCode.SUCCESS,"删除角色成功");
    }

    @Override
    public Result<Map<String, Object>> findRoleByUserId(Long userId) {
        if(StrUtil.isBlank(String.valueOf(userId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的用户 id 不能为空");
        }

        // 获取用户已分配的角色信息。
        Role role = userAndRoleMapper.getRoleByUserId(userId);

        // 获取所有的角色信息。
        List<Role> allRoles = roleMapper.getAllRoles();

        // 将结果封装并返回。
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignedRole", role);
        roleMap.put("allRolesList", allRoles);
        return Result.ok(roleMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> assignRole(UserAssignRoleVo userAssignRoleVo) {
        // 检查中间表是否已存在分配信息，存在则先删除信息。
        Long userId = userAssignRoleVo.getUserId();
        Role quriedRole = userAndRoleMapper.getRoleByUserId(userId);
        if(quriedRole != null){
            userAndRoleMapper.deleteUserRoleByUserId(userId);
        }

        // 校验分配的角色 id 是否存在。
        Long roleId = userAssignRoleVo.getRoleId();
        Role arole = roleMapper.getRoleByRoleId(roleId);
        if (arole == null){
            return Result.build(HttpCode.FAILURE,"分配的角色不存在");
        }

        // 重新分配用户和角色信息。
        UserAndRole userAndRole = new UserAndRole(userId, roleId);
        userAndRoleMapper.insertUserRole(userAndRole);
        return Result.build(HttpCode.SUCCESS,"用户分配角色信息成功");
    }

}
