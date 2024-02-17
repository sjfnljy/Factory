package com.sjf.constant;

import lombok.Getter;

/**
 * @Description: 工单优先级常量枚举类
 * @Author: SJF
 * @Date: 2024/1/14 16:17
 */

@Getter
public enum ProducePriority {
    STOPPED(0),
    PROGRESSING(1),
    URGENT(2);

    private final int value;

    ProducePriority(int value) {
        this.value = value;
    }

    public static boolean isValidType(int value) {
        for (ProducePriority type : ProducePriority.values()) {
            if (type.getValue() == value) {
                return true;
            }
        }
        return false;
    }
}
