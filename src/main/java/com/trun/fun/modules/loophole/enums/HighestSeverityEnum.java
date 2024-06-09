package com.trun.fun.modules.loophole.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.trun.fun.framework.emuns.IEnum;
import com.trun.fun.common.exception.UnknownEnumException;

public enum HighestSeverityEnum implements IEnum {
    /**
     * 低危
     */
    LOW(1),
    /**
     * 中等
     */
    MEDIUM(2),
    /**
     * 高危
     */
    HIGH(3),
    /**
     * 危机
     */
    CRITICAL(4);

    @EnumValue
    private final int value;

    HighestSeverityEnum(final int value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static HighestSeverityEnum getEnum(int value) {
        for (HighestSeverityEnum highestSeverityEnum : HighestSeverityEnum.values()) {
            if (highestSeverityEnum.getValue() == value) {
                return highestSeverityEnum;
            }
        }
        throw new UnknownEnumException("Error: Invalid HighestSeverityEnum type value: " + value);
    }
}
