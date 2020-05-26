package com.github.gaoqisen.webcenter.api;


import com.alibaba.fastjson.JSONObject;
import com.github.gaoqisen.webcenter.constant.ServerContextConstant;
import com.github.gaoqisen.webcenter.http.HttpUtil;
import com.github.gaoqisen.webcenter.pojo.Result;
import com.github.gaoqisen.webcenter.pojo.SysUser;
import com.github.gaoqisen.webcenter.pojo.WebCenterConsole;
import com.github.gaoqisen.webcenter.utils.DigestUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("sys")
public class ApiController {


    @Autowired
    private WebCenterConsole webCenterConsole;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @PostMapping("logout")
    @ApiOperation("退出功能")
    public Result logout(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        SysUser sysUser = getSysUserBySessionId(sessionId);
        if(sysUser == null) {
            return Result.error("获取用户失败");
        }
        String redisKeySet = "USER_LOGIN_SET".concat(String.valueOf(sysUser.getUserId()));
        Set<String> redisUsers = stringRedisTemplate.opsForSet().members(redisKeySet);
        for(String userLoginKey : redisUsers) {
            stringRedisTemplate.delete(userLoginKey);
        }
        stringRedisTemplate.delete(redisKeySet);
        return Result.success();
    }

    private SysUser getSysUserBySessionId(String sessionId) {
        String redisLoginKey = webCenterConsole.getCurrentApplicationName().toUpperCase().concat(sessionId);
        String redisUserInfo = stringRedisTemplate.opsForValue().get(redisLoginKey);
        if(redisUserInfo == null || redisUserInfo.isEmpty()) {
            return null;
        }
        return JSONObject.parseObject(redisUserInfo, SysUser.class);
    }

    @GetMapping("login")
    @ApiOperation("当前系统的登录")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        String redisStr = stringRedisTemplate.opsForValue().get(sessionId);
        try {
            String redirect = HttpUtil.urlJoint(request.getServerName(), request.getServerPort(),
                    contextPath.concat(ServerContextConstant.SYS_CALLBACK));
            if(webCenterConsole.getForestage()) {
                redirect = HttpUtil.urlJoint(webCenterConsole.getForestageHost(), webCenterConsole.getForestagePort(),
                        contextPath.concat(ServerContextConstant.SYS_CALLBACK));
            }
            String context = ServerContextConstant.LOGIN_CONTEXT.replace("CLIENTID", webCenterConsole.getClientId())
                    .replace("REDIRECT", redirect);
            String sendRedirect = HttpUtil.urlJoint(webCenterConsole.getHost(), webCenterConsole.getPort(), context);
            if (redisStr == null || redisStr.isEmpty()) {
                response.sendRedirect(sendRedirect);
            } else {
                // 用户登录过，直接重定向到redirect
                response.sendRedirect(redirect);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("callback")
    @ApiOperation("OSS回调地址")
    public void callback (String code, HttpServletRequest request, HttpServletResponse response) {

        String sign = DigestUtils.getDigest(code + webCenterConsole.getClientId() + webCenterConsole.getSecretKey());
        String userInfo = stringRedisTemplate.opsForValue().get(sign);
        if(userInfo == null) {
            return;
        }
        SysUser sysUser = JSONObject.parseObject(userInfo, SysUser.class);
        String sessionId = request.getSession().getId();

        String redisLoginKey = webCenterConsole.getCurrentApplicationName().toUpperCase().concat(sessionId);
        stringRedisTemplate.opsForValue().set(redisLoginKey,
                userInfo, 10, TimeUnit.HOURS);

        String userLoginSetKey = "USER_LOGIN_SET".concat(String.valueOf(sysUser.getUserId()));
        stringRedisTemplate.opsForSet().add(userLoginSetKey, redisLoginKey);

        String redirect = HttpUtil.urlJoint(request.getServerName(), request.getServerPort(), "/");
        if(webCenterConsole.getForestage()) {
            redirect = HttpUtil.urlJoint(webCenterConsole.getForestageHost(), webCenterConsole.getForestagePort(), "/");
        }
        try {
            response.sendRedirect(redirect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("menu/nav")
    @ApiOperation("获取当前登录账号的菜单信息")
    public Result navigation(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        SysUser sysUser = getSysUserBySessionId(sessionId);
        if(sysUser == null) {
            return Result.error("获取用户失败");
        }
        return Result.success().put("menuList", sysUser.getMenuList())
                .put("permissions", sysUser.getPermissions()).put("username", sysUser.getUsername());
    }

}
