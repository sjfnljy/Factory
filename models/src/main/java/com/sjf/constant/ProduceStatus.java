package com.sjf.constant;

import lombok.Getter;

/**
 * @Description: 工单状态枚举类
 * @Author: SJF
 * @Date: 2024/2/5 22:03
 */

@Getter
public enum ProduceStatus {
    UNSTART(0),
    EXECUTING(1),
    CANCELLED(2),
    ENDED(3),
    COMPLETED(4);

    private final int value;

    ProduceStatus(int value) {
        this.value = value;
    }

    public static boolean isValidStatus(int value) {
        for (ProduceStatus status : ProduceStatus.values()) {
            if (status.getValue() == value) {
                return false;
            }
        }
        return true;
    }
}
