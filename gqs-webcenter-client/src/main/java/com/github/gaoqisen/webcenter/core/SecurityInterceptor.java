package com.github.gaoqisen.webcenter.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.gaoqisen.webcenter.constant.ServerContextConstant;
import com.github.gaoqisen.webcenter.constant.SysPrompt;
import com.github.gaoqisen.webcenter.http.HttpUtil;
import com.github.gaoqisen.webcenter.pojo.Result;
import com.github.gaoqisen.webcenter.pojo.SysRest;
import com.github.gaoqisen.webcenter.pojo.SysUser;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 获取系统的权限，判断当前接口是否需要权限
        String redisSysRest = stringRedisTemplate.opsForValue().get(applicationName);
        List<SysRest> list = JSONObject.parseArray(redisSysRest, SysRest.class);
        SysRest sysRest = null;
        for (int i = 0; i < list.size(); i++) {
            if(request.getServletPath().equals(list.get(i).getUrl())) {
                sysRest = list.get(i);
            }
        }
        if(sysRest != null) {
            // 获取当前用户的权限,判断当前用户是否有权限
            String sessionId = request.getSession().getId();
            String userInfo = stringRedisTemplate.opsForValue().get(applicationName.toUpperCase().concat(sessionId));
            String restPermission = sysRest.getPermissions();
            if ("authc".equals(restPermission)) {
                if(userInfo == null || userInfo.isEmpty()) {
                    responseOut(response, SysPrompt.NO_LOGIN);
                    return false;
                }
            }
            if(restPermission.startsWith("perms")) {
                // 判断是否有权限
                SysUser sysUser = JSONObject.parseObject(userInfo, SysUser.class);
                Set<String> stringSet = sysUser.getPermissions();
                for(String perm : stringSet) {
                    if(restPermission.contains(perm)) {
                        return true;
                    }
                }
                responseOut(response,SysPrompt.NO_PERMISSION);
                return false;
            }
        }
        return true;
    }

    private void responseOut(HttpServletResponse response, String msg) {
        try {
            response.setStatus(200);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(Result.error(msg)));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}
