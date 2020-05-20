package com.gaoqisen.webcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.constant.Constant;
import com.gaoqisen.webcenter.entity.SysUser;
import com.gaoqisen.webcenter.exception.AppException;
import com.gaoqisen.webcenter.form.LoginForm;
import com.gaoqisen.webcenter.service.SysUserService;
import com.gaoqisen.webcenter.utils.Result;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
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


    @ApiOperation("系统退出")
    @PostMapping("logout")
    public Result logout() {
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

        String captcha = stringRedisTemplate.opsForValue().get(Constant.CAPTCHA.concat(httpServletRequest.getSession().getId()));
        if(!loginForm.getCaptcha().equalsIgnoreCase(captcha)) {
            return Result.error("验证码输入错误");
        }

        try{
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(loginForm.getUsername(),
                    loginForm.getPassword());
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

        return Result.success().put("username", loginForm.getUsername());
    }

    @ApiOperation("API登录")
    @PostMapping(value = "/api/login")
    public Result apiLogin(@RequestBody LoginForm loginForm, HttpServletRequest httpServletRequest){
        Result result = verifyForm(loginForm);
        if (result != null) return result;

        String captcha = stringRedisTemplate.opsForValue().get(Constant.CAPTCHA.concat(httpServletRequest.getSession().getId()));
        if(!loginForm.getCaptcha().equalsIgnoreCase(captcha)) {
            return Result.error("验证码输入错误");
        }

        SysUser sysUser = this.sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", loginForm.getUsername()));
        if(sysUser == null || !sysUser.getPassword().equals(new Sha256Hash(loginForm.getPassword(), sysUser.getSalt()).toHex())){
            return Result.error("帐号和密码不正确");
        }
        return Result.success();
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


}
