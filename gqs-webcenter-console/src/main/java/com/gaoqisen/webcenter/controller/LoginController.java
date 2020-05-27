package com.gaoqisen.webcenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.constant.Constant;
import com.gaoqisen.webcenter.constant.SysContextConstant;
import com.gaoqisen.webcenter.entity.SysCode;
import com.gaoqisen.webcenter.entity.SysMenu;
import com.gaoqisen.webcenter.entity.SysUser;
import com.gaoqisen.webcenter.exception.AppException;
import com.gaoqisen.webcenter.form.LoginForm;
import com.gaoqisen.webcenter.service.SysCodeService;
import com.gaoqisen.webcenter.service.SysMenuService;
import com.gaoqisen.webcenter.service.SysRestService;
import com.gaoqisen.webcenter.service.SysUserService;
import com.gaoqisen.webcenter.utils.DigestUtils;
import com.gaoqisen.webcenter.utils.HttpUtil;
import com.gaoqisen.webcenter.utils.Result;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("main")
public class LoginController {

    @Autowired
    private Producer producer;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysCodeService sysCodeService;

    @Autowired
    private SysRestService sysRestService;

    @Autowired
    private SysMenuService sysMenuService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${webcenter.client.forestage:false}")
    private Boolean forestage;

    @Value("${webcenter.client.host}")
    private String forestageHost;

    @Value("${webcenter.client.port}")
    private Integer forestagePort;

    @ApiOperation("系统退出")
    @PostMapping("logout")
    public Result logout(HttpServletRequest httpRequest) {
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        String redisKeySet = "USER_LOGIN_SET".concat(String.valueOf(sysUser.getUserId()));
        Set<String> redisUsers = stringRedisTemplate.opsForSet().members(redisKeySet);
        for(String userLoginKey : redisUsers) {
            stringRedisTemplate.delete(userLoginKey);
        }
        stringRedisTemplate.delete(redisKeySet);
        SecurityUtils.getSubject().logout();
        return Result.success();
    }

    @ApiOperation("验证码")
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, String uuid) throws IOException {
        if(StringUtils.isBlank(uuid)) {
            throw new AppException("uuid不能为空");
        }
        String text = producer.createText();
        stringRedisTemplate.opsForValue().set(Constant.CAPTCHA.concat(httpRequest.getSession().getId()), text, 30, TimeUnit.MINUTES);
        BufferedImage bufferedImage = producer.createImage(text);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        ImageIO.write(bufferedImage, "jpg", servletOutputStream);
        IOUtils.closeQuietly(servletOutputStream);
    }

    @ApiOperation("系统登录")
    @PostMapping(value = "login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest httpServletRequest){

        Result result = verifyForm(loginForm);
        if (result != null) return result;

        String captcha = stringRedisTemplate.opsForValue().get(Constant.CAPTCHA.concat(httpServletRequest.getSession().getId())).toString();
        if(!loginForm.getCaptcha().equalsIgnoreCase(captcha)) {
            return Result.error("验证码输入错误");
        }
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", loginForm.getUsername()));
        //账号不存在
        if(sysUser == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        //账号锁定
        if(sysUser.getStatus() == 0){
            return Result.error("账号已被锁定,请联系管理员");
        }

        Result x = shiroLogin(loginForm.getUsername(), loginForm.getPassword());
        if (x != null) return x;

        sysUser.setMenuList(sysMenuService.queryMenuByUserIdAndApplicationName(sysUser.getUserId(), applicationName));
        sysUser.setPermissionList(sysRestService.getUrlByUserIdAndApplicationName(sysUser.getUserId(), applicationName));
        String redisLoginKey = applicationName.toUpperCase().concat(httpServletRequest.getSession().getId());
        stringRedisTemplate.opsForValue().set(redisLoginKey,
                JSON.toJSONString(sysUser), 10, TimeUnit.HOURS);

        String userLoginSetKey = "USER_LOGIN_SET".concat(String.valueOf(sysUser.getUserId()));
        stringRedisTemplate.opsForSet().add(userLoginSetKey, redisLoginKey);
        stringRedisTemplate.expire(userLoginSetKey, 10, TimeUnit.HOURS);
        return Result.success().put("username", loginForm.getUsername());
    }

    private Result shiroLogin(String username, String password) {
        try{
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        }catch (UnknownAccountException e) {
            return Result.error("帐号或者密码错误");
        }catch (IncorrectCredentialsException e) {
            return Result.error("帐号和密码错误");
        }catch (LockedAccountException e) {
            return Result.error("帐号和密码错误");
        }catch (AuthenticationException e) {
            return Result.error("账户验证失败");
        }
        return null;
    }

    @ApiOperation("单点登录判断")
    @RequestMapping(value = "/sso/login", method = RequestMethod.GET)
    public void ssoLogin(HttpServletRequest request, HttpServletResponse response, String redirect, String clientId){
        // 校验clientId
        SysCode sysCode = sysCodeService.getOne(new QueryWrapper<SysCode>().eq("client_id", clientId));
        if(sysCode == null) {
            responseOut(response, "非法ClientId");
            return;
        }
        String sessionId = request.getSession().getId();
        Object redisStr = stringRedisTemplate.opsForValue().get(applicationName.toUpperCase().concat(sessionId));
        try {
            if (redisStr == null) {
                String context = SysContextConstant.LOGIN_CONTEXT.replace("CLIENTID", clientId).replace("REDIRECT", redirect);
                String redirectStr = HttpUtil.urlJoint(request.getServerName(), request.getServerPort(), context);
                if(forestage) {
                    redirectStr = HttpUtil.urlJoint(forestageHost, forestagePort, context);
                }
                response.sendRedirect(redirectStr);
            } else {
                // 用户登录过，直接生成code重定向到redirect
                String code = UUID.randomUUID().toString();
                String sign = DigestUtils.getDigest(code + clientId + sysCode.getSecretKey());
                SysUser sysUser = JSONObject.parseObject(redisStr.toString(), SysUser.class);
                sysUser.setMenuList(sysMenuService.queryMenuByUserIdAndApplicationName(sysUser.getUserId(), sysCode.getApplicationName()));
                sysUser.setPermissionList(sysRestService.getUrlByUserIdAndApplicationName(sysUser.getUserId(), sysCode.getApplicationName()));
                stringRedisTemplate.opsForValue().set(sign, JSON.toJSONString(sysUser), 5, TimeUnit.MINUTES);
                response.sendRedirect(redirect.concat("?code=").concat(code));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("单点登录")
    @PostMapping(value = "/sso/login")
    public Result ssoLogin(@RequestBody LoginForm loginForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        // 校验clientId
        SysCode sysCode = sysCodeService.getOne(new QueryWrapper<SysCode>().eq("client_id", loginForm.getClientId()));
        if(sysCode == null) {
            return Result.error("客服端ID不正确");
        }
        String sessionId = httpServletRequest.getSession().getId();
        String captcha = stringRedisTemplate.opsForValue().get(Constant.CAPTCHA.concat(httpServletRequest.getSession().getId())).toString();
        if(!loginForm.getCaptcha().equalsIgnoreCase(captcha)) {
            return Result.error("验证码输入错误");
        }

        //用户信息
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", loginForm.getUsername()));

        //账号不存在、密码错误
        String cpass = DigestUtils.getDigest(loginForm.getPassword() + user.getSalt());
        if(user == null || !user.getPassword().equals(cpass)) {
            return Result.error("账号或密码不正确");
        }

        //账号锁定
        if(user.getStatus() == 0){
            return Result.error("账号已被锁定,请联系管理员");
        }

        Result x = shiroLogin(loginForm.getUsername(), loginForm.getPassword());
        if (x != null) return x;

        user.setMenuList(sysMenuService.queryMenuByUserIdAndApplicationName(user.getUserId(), sysCode.getApplicationName()));
        user.setPermissionList(sysRestService.getUrlByUserIdAndApplicationName(user.getUserId(), sysCode.getApplicationName()));

        //生成code，把code返回给调用方。之后通过code可以获取用户信息
        String code = UUID.randomUUID().toString();
        String sign = DigestUtils.getDigest(code + loginForm.getClientId() + sysCode.getSecretKey());
        // 签名缓存3分钟
        stringRedisTemplate.opsForValue().set(sign, JSON.toJSONString(user), 3, TimeUnit.MINUTES);
        String redisLoginKey = applicationName.toUpperCase().concat(sessionId);
        // 当前系统登录信息缓存10小时
        stringRedisTemplate.opsForValue().set(redisLoginKey,JSON.toJSONString(user), 10, TimeUnit.HOURS);
        // 系统登录key记录
        String userLoginSetKey = "USER_LOGIN_SET".concat(String.valueOf(user.getUserId()));
        stringRedisTemplate.opsForSet().add(userLoginSetKey, redisLoginKey);
        stringRedisTemplate.expire(userLoginSetKey, 10, TimeUnit.HOURS);
        return Result.success().putData(code);
    }

    private Result verifyForm(@RequestBody LoginForm loginForm) {
        if(StringUtils.isBlank(loginForm.getCaptcha())) {
            return Result.error("验证码获取失败");
        }
        if(StringUtils.isBlank(loginForm.getPassword())) {
            return Result.error("密码获取失败");
        }
        if(StringUtils.isBlank(loginForm.getUsername())) {
            return Result.error("用户名获取失败");
        }
        return null;
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


}
