package com.sjf.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.log.OperationLog;
import org.springframework.stereotype.Repository;

/**
 * @Description: 操作日志 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/19 20:43
 */

@Repository
public interface SysOperLogMapper extends BaseMapper<OperationLog> {

}
