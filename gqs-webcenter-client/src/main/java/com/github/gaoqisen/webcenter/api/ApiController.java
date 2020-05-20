package com.github.gaoqisen.webcenter.api;


import com.alibaba.fastjson.JSON;
import com.github.gaoqisen.webcenter.pojo.Result;
import com.github.gaoqisen.webcenter.pojo.WebCenterConsole;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("sys")
public class ApiController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private WebCenterConsole webCenterConsole;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("login")
    @ApiOperation("当前系统的登录")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        String redisStr = stringRedisTemplate.opsForValue().get(sessionId);
        try {
            String redirect = request.getRequestURI().concat("#home");
            if (redisStr == null || redisStr.isEmpty()) {
                response.sendRedirect("http://".concat(webCenterConsole.getHost()).concat(":").concat(webCenterConsole.getPort()) + "/#/login?clientId=" + webCenterConsole.getClientId() + "&redirect="+ redirect);
            } else {
                // 用户登录过，直接重定向到redirect
                response.sendRedirect(redirect);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("nav")
    @ApiOperation("获取当前登录账号的菜单信息")
    public Result navigation(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        String redisStr = stringRedisTemplate.opsForValue().get(sessionId);
        if(redisStr == null || redisStr.isEmpty()) {
            return Result.error("菜单获取失败");
        }
        return Result.success().put("nav", JSON.toJSON(redisStr));
    }

}
