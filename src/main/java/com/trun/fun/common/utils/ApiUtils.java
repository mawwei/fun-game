package com.trun.fun.common.utils;

import com.trun.fun.common.constant.APICons;
import com.trun.fun.common.spring.ApplicationUtils;
import com.trun.fun.framework.utils.ApiAssert;
import com.trun.fun.framework.emuns.ErrorCodeEnum;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * API工具类
 *
 * @author Mawei
 */
@SuppressWarnings("ALL")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public abstract class ApiUtils {

    /**
     * 获取当前用户id
     */
    public static Integer currentUid() {
        Integer uid = (int) ApplicationUtils.getRequest().getAttribute(APICons.API_UID);
        if (Objects.isNull(uid)) {
            String token = ApplicationUtils.getRequest().getHeader("Authorization");
            ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, token);
            token = token.replaceFirst("Bearer ", "");
            Claims claims = JWTUtils.getClaim(token);
            ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, claims);
            return claims.get(JWTUtils.UID, Integer.class);
        }
        return uid;
    }



}
