package com.trun.fun.common.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.trun.fun.common.exception.UnknownEnumException;
import com.trun.fun.framework.emuns.IEnum;

/**
 * 数据脱敏枚举类
 * mawei
 */
public enum DesensitizationTypeEnum  implements IEnum {
    /**
     * 中文名
     */
    DEFAULT(0),
    /**
     * 中文名
     */
    CHINESE_NAME(1),
    /**
     * 身份证号
     */
    ID_CARD(2),
    /**
     * 座机号
     */
    FIXED_PHONE(3),
    /**
     * 手机号
     */
    MOBILE_PHONE(4),
    /**
     * 地址
     */
    ADDRESS(5),
    /**
     * 电子邮件
     */
    EMAIL(6),
    /**
     * 银行卡
     */
    BANK_CARD(7),
    /**
     * 公司开户银行联号
     */
    CNAPS_CODE(8),
    /**
     * IP地址
     */
    IP_LIST(9);
    @EnumValue
    private final int value;


    DesensitizationTypeEnum(final int value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public int getValue() {
        return 0;
    }

    @JsonCreator
    public static DesensitizationTypeEnum getEnum(int value) {
        for (DesensitizationTypeEnum sensitiveTypeEnum : DesensitizationTypeEnum.values()) {
            if (sensitiveTypeEnum.getValue() == value) {
                return sensitiveTypeEnum;
            }
        }
        throw new UnknownEnumException("Error: Invalid sensitiveTypeEnum type value: " + value);
    }
}
