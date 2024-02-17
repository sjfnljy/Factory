package com.sjf.constant;

import lombok.Getter;

/**
 * @Description: 产品类型常量枚举类
 * @Author: SJF
 * @Date: 2024/1/14 16:17
 */
@Getter
public enum ProduceType {
    SELF_PRODUCE(0),
    COMMISSIONED_PRODUCE(1),
    OUT_SOURCING(2);

    private final int value;

    ProduceType(int value) {
        this.value = value;
    }

    public static boolean isValidType(int value) {
        for (ProduceType type : ProduceType.values()) {
            if (type.getValue() == value) {
                return true;
            }
        }
        return false;
    }
}
