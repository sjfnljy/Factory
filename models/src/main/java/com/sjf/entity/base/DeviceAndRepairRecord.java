package com.sjf.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 设备和维修记录中间表实体类
 * @Author: SJF
 * @Date: 2024/1/22 14:52
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_device_repair")
public class DeviceAndRepairRecord extends BaseEntity {

    @TableField("device_id")
    private Long deviceId;

    @TableField("repair_id")
    private Long repairId;


    public DeviceAndRepairRecord(Long deviceId, Long repairId) {
        this.deviceId = deviceId;
        this.repairId = repairId;
    }
}
