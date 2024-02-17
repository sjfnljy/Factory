package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.DeviceAndRepairRecord;
import com.sjf.entity.base.RepairRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 设备和维修记录中间表 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/22 14:54
 */

@Repository
public interface DeviceAndRepairMapper extends BaseMapper<DeviceAndRepairRecord> {

    List<Long> getRepairRecordIdsByDeviceId(@Param("device_id") Long deviceId);

    void deleteDeviceAndRepairByDeviceId(@Param("device_id") Long deviceId);

    List<RepairRecord> getRepairRecordByDeviceId(@Param("device_id") Long deviceId);
}
