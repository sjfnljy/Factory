package com.sjf.entity.base;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 工厂设备实体类
 * @Author: SJF
 * @Date: 2024/1/19
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_device")
public class Device extends BaseEntity {

    @TableField("device_name")
    @ExcelProperty(value = "设备名称", index = 0)
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @TableField("device_code")
    @ExcelProperty(value = "设备编号", index = 1)
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @TableField("manufacturer")
    @ExcelProperty(value = "设备厂家", index = 2)
    @ApiModelProperty(value = "设备厂家")
    private String manufacturer;

    @TableField("status")
    @ExcelProperty(value = "设备状态", index = 3)
    @ApiModelProperty(value = "设备状态")
    private Integer deviceStatus;

    @TableField(exist = false)
    @ExcelIgnore
    private List<Long> repairRecordIdList;
}
