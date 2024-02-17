package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.RepairRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @Description: 维修记录 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/22 16:23
 */

@Repository
public interface RepairRecordMapper extends BaseMapper<RepairRecord> {
    RepairRecord getRepairRecordByCode(@Param("repair_code") String repairCode);

    RepairRecord getRepairRecordById(@Param("repair_id") Long repairId);
}
