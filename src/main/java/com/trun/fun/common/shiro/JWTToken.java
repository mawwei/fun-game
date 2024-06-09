package com.trun.fun.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JWTToken
 *
 * @author Mawei
 */
public class JWTToken implements AuthenticationToken {

    private final String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}