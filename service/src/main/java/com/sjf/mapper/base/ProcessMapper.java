package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.Process;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 工序 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/23 17:27
 */

@Repository
public interface ProcessMapper extends BaseMapper<Process> {

    Process getProcessById(@Param("process_id") Long processId);

    Process getProcessByCode(@Param("process_code") String processCode);

    void updateProcessStatus(@Param("process_id") Long processId, @Param("status")Integer status);

    void deleteProcess(@Param("process_id") Long processId);
}
