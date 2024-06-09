package com.trun.fun.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.trun.fun.common.exception.UnknownEnumException;
import com.trun.fun.framework.emuns.IEnum;

/**
 * 枚举类
 * 日志类型：MANUAL主动调用、AUTO公共
 */
public enum LogTypeEnum implements IEnum {
    MANUAL(1),
    AUTO(2);

    @EnumValue
    private final int value;

    LogTypeEnum(int value) {
        this.value = value;
    }

    @JsonCreator
    public static LogTypeEnum getEnum(int value) {
        for (LogTypeEnum logTypeEnum : LogTypeEnum.values()) {
            if (logTypeEnum.getValue() == value) {
                return logTypeEnum;
            }
        }
        throw new UnknownEnumException("Error: Invalid AuthTypeEnum type value: " + value);
    }

    @Override
    public int getValue() {
         return this.value;
    }
}
