package com.trun.fun.common.annotations;

import com.trun.fun.common.validtor.EnumValidtor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 枚举类型检验注解
 * 用于检验前端传过来的值是否是固定枚举范围值
 *@author mawei
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidtor.class})
@Documented
public @interface EnumValidAnnotation {
        /**
         * 错误消息值
         * @return
         */
        String message() default "";



        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        /**
         * 枚举类CLASS
         * @return
         */
        Class<?>[] target() default {};
}