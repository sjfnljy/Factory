package com.sjf.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.admin.Role;
import com.sjf.entity.admin.UserAndRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 系统用户和角色中间表 Mapper 接口
 * @Author: SJF
 * @Date: 2023/1/9
 **/

@Repository
public interface UserAndRoleMapper extends BaseMapper<UserAndRole> {
    List<Integer> getUserIdByRoleId(@Param("role_id") Long roleId);

    Role getRoleByUserId(@Param("user_id") Long userId);

    Long getRoleIdByUserId(@Param("user_id") Long userId);

    void insertUserRole(UserAndRole userAndRole);

    void deleteUserRoleByUserId(@Param("user_id") Long userId);
}
