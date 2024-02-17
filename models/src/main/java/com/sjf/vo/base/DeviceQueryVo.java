package com.sjf.vo.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 设备条件查询传输类
 * @Author: SJF
 * @Date: 2023/1/20
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String deviceName;

    private String deviceCode;

    private Integer deviceStatus;
}
