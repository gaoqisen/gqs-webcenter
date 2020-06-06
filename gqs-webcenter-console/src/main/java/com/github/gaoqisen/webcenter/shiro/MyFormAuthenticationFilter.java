package com.github.gaoqisen.webcenter.shiro;

import com.alibaba.fastjson.JSON;
import com.github.gaoqisen.webcenter.utils.Result;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write(JSON.toJSONString(Result.logout("请登录后访问")));
        out.flush();
        out.close();
        return false;
    }

}
