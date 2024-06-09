package com.trun.fun.common.annotations;

import com.trun.fun.common.enums.AuthTypeEnum;

import java.lang.annotation.*;

/**
 * 权限认证注解
 *
 * @author Mawei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resources {

    /**
     * 权限认证类型
     *
     * @see AuthTypeEnum
     */
    AuthTypeEnum auth() default AuthTypeEnum.AUTH;
}
