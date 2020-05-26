package com.gaoqisen.webcenter.shiro;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.entity.SysUser;
import com.gaoqisen.webcenter.service.SysMenuService;
import com.gaoqisen.webcenter.service.SysRestService;
import com.gaoqisen.webcenter.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class MyShiroRealm  extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRestService sysRestService;

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     *登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", username));
        //账号不存在
        if(sysUser == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        //密码错误
        if(!new Sha256Hash(password, sysUser.getSalt()).toHex().equals(sysUser.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }

        //账号锁定
        if(sysUser.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(sysUser, password, getName());
        return info;
    }
    /**
     * 用户访问授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        authorizationInfo.setStringPermissions(this.sysRestService.
                getPermissionsByUserIdAndApplicationName(user.getUserId(), springApplicationName));
        return authorizationInfo;
    }

}
