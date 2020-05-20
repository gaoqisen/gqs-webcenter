package com.gaoqisen.webcenter.shiro;

import com.alibaba.fastjson.JSON;
import com.gaoqisen.webcenter.utils.Result;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 登录后返回json数据
 */
public class ShiroUserFilter extends UserFilter {

    @Override
    protected void redirectToLogin(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        // 返回json
        servletResponse.setContentType("application/json; charset=utf-8");
        String json = JSON.toJSONString(Result.error("请登录后访问"));
        servletResponse.getWriter().write(json);
    }
}
