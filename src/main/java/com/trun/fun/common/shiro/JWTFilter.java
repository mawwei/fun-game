package com.trun.fun.common.shiro;

import com.trun.fun.common.constant.APICons;
import com.trun.fun.framework.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import com.trun.fun.framework.emuns.ErrorCodeEnum;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * JWT过滤器 适用于shiro
 *
 * @author Mawei
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private PathMatcher pathMatcher;
    private UrlPathHelper urlPathHelper;
    private String contextPath;


    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        //获取请求token
        String token = getToken(WebUtils.toHttp(servletRequest));
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return StringUtils.isBlank(token) ? null : new JWTToken(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        request.setAttribute(APICons.API_BEGIN_TIME, System.currentTimeMillis());
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);

        String token = getToken(httpRequest);
        String method = httpRequest.getMethod();
        String requestUri = urlPathHelper.getOriginatingRequestUri(httpRequest);

        if (StringUtils.isNotEmpty(contextPath)) {
            requestUri = requestUri.replaceFirst(contextPath, "");
        }

       //

//        if (Objects.isNull(token)) {
//            List<ResourcePermDTO> openPerms = resourceService.getOpenPerms();
//            boolean match = anyMatch(openPerms, method, requestUri);
//            if (!match) {
//                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            }
//            return match;
//        }
//        if (isLoginRequest(request, response)) {
//            if (executeLogin(request, response)) {
//                Integer uid = JWTUtils.getUid(token);
//                request.setAttribute(APICons.API_UID, uid);
//                request.setAttribute(APICons.API_USERINFO,userInfoService.getById(uid));
//                Set<ResourcePermDTO> perms = resourceService.getUserResourcePerms(uid);
//                return anyMatch(perms, method, requestUri);
//            } else {
//                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return sendUnauthorizedFail(request, response);
//            }
//        }
        return true;

    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        switch (httpResponse.getStatus()) {
            case HttpServletResponse.SC_NOT_FOUND:
                return sendNotFoundFail(request, response);
            case HttpServletResponse.SC_UNAUTHORIZED:
                return sendUnauthorizedFail(request, response);
            default:
                return sendForbiddenFail(request, response);
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return sendUnauthorizedFail(request, response);
    }

    /**
     * 获取请求的token
     */
    protected String getToken(HttpServletRequest request) {
        //从header中获取token
        String token = request.getHeader(AUTHORIZATION_HEADER);
        return StringUtils.isBlank(token) ? null : token.replaceFirst("Bearer ", "");
    }

    /**
     * 无权限
     */
    protected boolean sendForbiddenFail(ServletRequest request, ServletResponse response) {
        ResponseUtils.sendFail(WebUtils.toHttp(request), WebUtils.toHttp(response), ErrorCodeEnum.FORBIDDEN);
        return false;
    }

    /**
     * 路径不存在
     */
    protected boolean sendNotFoundFail(ServletRequest request, ServletResponse response) {
        ResponseUtils.sendFail(WebUtils.toHttp(request), WebUtils.toHttp(response), ErrorCodeEnum.NOT_FOUND);
        return false;
    }

    /**
     * 未认证
     */
    protected boolean sendUnauthorizedFail(ServletRequest request, ServletResponse response) {
        ResponseUtils.sendFail(WebUtils.toHttp(request), WebUtils.toHttp(response), ErrorCodeEnum.UNAUTHORIZED);
        return false;
    }






    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        try {
            return super.executeLogin(request, response);
        } catch (Exception ignored) {
        }
        return false;
    }


    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}