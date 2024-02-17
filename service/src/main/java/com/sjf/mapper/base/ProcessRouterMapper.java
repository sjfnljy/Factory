package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.ProcessRouter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 工艺路线管理 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/24 21:34
 */

@Repository
public interface ProcessRouterMapper extends BaseMapper<ProcessRouter> {
    ProcessRouter getRouterById(@Param("router_id") Long routerId);

    ProcessRouter getRouterByRouterCode(@Param("router_code")String routerCode);

    void deleteRouterById(@Param("router_id") Long routerId);
}
