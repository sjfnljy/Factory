package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.Device;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 设备管理 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/20 16:36
 */

@Repository
public interface DeviceMapper extends BaseMapper<Device> {
    Device getDeviceByDeviceId(@Param("device_id") Long deviceId);

    Device getDeviceByDeviceName(@Param("device_name") String deviceName);

    Device getDeviceByDeviceCode(@Param("device_code") String deviceCode);

    void deleteDevice(@Param("device_id") Long deviceId);
}
