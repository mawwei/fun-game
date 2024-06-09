package com.trun.fun.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.trun.fun.framework.emuns.IEnum;

/**
 * <p>
 * 状态枚举
 * </p>
 *
 * @author Mawei
 */
public enum StatusEnum implements IEnum {

    NORMAL(0), DISABLE(1);

    @EnumValue
    private final int value;

    StatusEnum(final int value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public int getValue() {
        return this.value;
    }
}
