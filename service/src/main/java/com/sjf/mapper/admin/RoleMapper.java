package com.sjf.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.admin.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 角色 Mapper 接口
 * @Author: SJF
 * @Date: 2023/1/7
 **/
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> getAllRoles();

    Role getRoleByRoleId(@Param("role_id") Long roleId);

    Role getRoleByRoleName(@Param("role_name") String roleName);

    Role getRoleByRoleCode(@Param("role_code")String roleCode);

    void insertRole(Role role);

    void updateRole(Role role);

    void deleteRoleById(@Param("role_id") Long roleId);
}
