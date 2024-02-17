package com.sjf.constant;

import lombok.Getter;

/**
 * @Description: 设备状态类别常量枚举类
 * @Author: SJF
 * @Date: 2024/1/14 16:17
 */

@Getter
public enum DeviceStatus {
    UNSTART(0),
    RUNNING(1),
    TROUBLING(2),
    REPAIRING(3);
    private final int value;

    DeviceStatus(int value) {
        this.value = value;
    }

    public static boolean isValidStatus(int value) {
        for (DeviceStatus status : DeviceStatus.values()) {
            if (status.getValue() == value) {
                return false;
            }
        }
        return true;
    }
}
