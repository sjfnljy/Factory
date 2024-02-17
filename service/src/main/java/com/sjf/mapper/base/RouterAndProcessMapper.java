package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.Process;
import com.sjf.entity.base.RouterAndProcess;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 工艺路线工序中间表实体类
 * @Author: SJF
 * @Date: 2024/1/24 21:39
 */

@Repository
public interface RouterAndProcessMapper extends BaseMapper<RouterAndProcess>  {
    List<Process> getProcessesByRouterId(@Param("router_id") Long routerId);

    void deleteRouterProcessByRouterId(@Param("router_id") Long routerId);
}
