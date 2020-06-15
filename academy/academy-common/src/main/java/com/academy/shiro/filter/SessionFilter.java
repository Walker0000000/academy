package com.academy.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.academy.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SessionFilter extends FormAuthenticationFilter {

    private final static String X_REQUESTED_WITH_STRING = "X-Requested-With";
    private final static String XML_HTTP_REQUEST_STRING = "XMLHttpRequest";

    /**
     * shiro认证perms资源失败后回调方法
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.info("----------权限控制-------------");
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (this.isLoginRequest(servletRequest, servletResponse)) {
            if (this.isLoginSubmission(servletRequest, servletResponse)) {
                return this.executeLogin(servletRequest, servletResponse);
            } else {
                return true;
            }
        }else {
            if (isAjax((HttpServletRequest) servletRequest)) {//如果是ajax返回指定格式数据;
                ResultUtil.error(403,"登陆过期");
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json");
                //返回禁止访问json字符串;
                httpServletResponse.getWriter().write(JSONObject.toJSONString(ResultUtil.error("权限不足")));
            } else {//如果是普通请求进行重定向
                log.info("----------普通请求拒绝-------------");
                httpServletResponse.sendRedirect("/403");
            }
            return false;
        }
    }

    public boolean isAjax(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader(X_REQUESTED_WITH_STRING);
        if (XML_HTTP_REQUEST_STRING.equalsIgnoreCase(header)) {
            log.debug("当前请求为Ajax请求:{}", httpServletRequest.getRequestURI());
            return Boolean.TRUE;
        }
        log.debug("当前请求非Ajax请求:{}", httpServletRequest.getRequestURI());
        return Boolean.FALSE;
    }
}
