package com.trun.fun.common.annotations;

import com.trun.fun.common.enums.DesensitizationTypeEnum;
import java.lang.annotation.*;
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
/**
 * 数据脱敏注解
 * @author mawei
 */
public @interface DesensitizationAnnotation {
        DesensitizationTypeEnum value();

        /*判断注解是否生效的方法*/
        String isEffictiveMethod() default "";
}
