package com.github.gaoqisen.webcenter.shiro;

import com.alibaba.fastjson.JSON;
import com.github.gaoqisen.webcenter.utils.Result;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        try {
            PrintWriter out = httpServletResponse.getWriter();
            out.println(JSON.toJSONString(Result.logout("权限不足")));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
