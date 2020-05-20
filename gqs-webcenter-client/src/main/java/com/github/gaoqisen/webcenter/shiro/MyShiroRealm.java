package com.github.gaoqisen.webcenter.shiro;


import com.github.gaoqisen.webcenter.constant.ServerContextConstant;
import com.github.gaoqisen.webcenter.http.HttpUtil;
import com.github.gaoqisen.webcenter.pojo.SysUser;
import com.google.common.collect.Maps;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyShiroRealm  extends AuthorizingRealm {

    /**
     *登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        // TODO 用http在console中获取用户信息
        HashMap hashMap = Maps.newHashMap();
        hashMap.put("username", username);
        SysUser sysUser = HttpUtil.parseObject(ServerContextConstant.API_USER_INFO, hashMap, SysUser.class);
        if(sysUser == null){
            return null;
        }
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
        // TODO 通过用户ID和应用名获取用户权限
        HashMap hashMap = Maps.newHashMap();
        hashMap.put("userId", user.getUserId());
        List<String> list = HttpUtil.parseArray(ServerContextConstant.API_CURRENT_USER_PERM,
                hashMap, String.class);
        authorizationInfo.setStringPermissions(new HashSet<>(list));
        return authorizationInfo;
    }

}
