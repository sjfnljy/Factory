package com.sjf.constant;

import lombok.Getter;

/**
 * @Description: 工序状态常量枚举类
 * @Author: SJF
 * @Date: 2024/1/14 16:17
 */

@Getter
public enum ProcessStatus {

    UNSTART(0),
    EXECUTING(1),
    CANCELLED(2),
    ENDED(3),
    COMPLETED(4);

    private final int value;

    ProcessStatus(int value) {
        this.value = value;
    }

    public static boolean isValidStatus(int value) {
        for (ProcessStatus status : ProcessStatus.values()) {
            if (status.getValue() == value) {
                return false;
            }
        }
        return true;
    }
}
