package com.gaoqisen.webcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.entity.SysMenu;
import com.gaoqisen.webcenter.entity.SysRest;
import com.gaoqisen.webcenter.entity.SysUser;
import com.gaoqisen.webcenter.service.SysMenuService;
import com.gaoqisen.webcenter.service.SysRestService;
import com.gaoqisen.webcenter.service.SysUserService;
import com.gaoqisen.webcenter.utils.DigestUtils;
import com.gaoqisen.webcenter.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SysRestService sysRestService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("通过应用名称和用户ID获取权限")
    @RequestMapping("/sys/rest/getCurrentUserPerm")
    public Result getCurrentUserPerm(@RequestBody String applicationName, @RequestBody Integer userId) {
        if(userId == null) {
            return Result.error("用户ID不能为空");
        }
        if(StringUtils.isBlank(applicationName)) {
            return Result.error("应用名称不能为空");
        }
        return Result.success().putData(this.sysRestService.
                getPermissionsByUserIdAndApplicationName(userId, applicationName));
    }

    @ApiOperation("通过用户名获取用户信息")
    @RequestMapping("/sys/user/info")
    public Result getUserInfo(@RequestBody String username) {
        if(StringUtils.isBlank(username)) {
            return Result.error("用户名不能为空");
        }
        return Result.success().putData(this.sysUserService.getMap(new QueryWrapper<SysUser>().eq("username", username)));
    }

    @RequestMapping("/sys/rest/permission")
    @ApiOperation("通过应用名称获取应用系统权限")
    public Result getSysPermission(@RequestBody String applicationName) {
        List<SysRest> list = this.sysRestService.list(new QueryWrapper<SysRest>().eq("application_name",applicationName));
        return Result.success().putData(list);
    }

    @ApiOperation("保存rest接口列表")
    @RequestMapping("/sys/rest/saves")
    public Result saveSysRestList(@RequestBody List<SysRest> restList) {
        if (restList == null) {
            return Result.error("接口类别获取失败");
        }
        for(SysRest sysRest : restList) {
            sysRest.setCreateTime(new Date());
            sysRest.setDigest(DigestUtils.getDigest(sysRest.getApplicationName().concat(sysRest.getUrl())));
            this.sysRestService.saveOrUpdate(sysRest);
        }
        return Result.success();
    }

    @PostMapping("/sys/nav/permission")
    public Result navigation(@RequestBody Integer userId, @RequestBody String applicationName) {
        // 通userId和应用名获取菜单
        List<SysMenu> sysMenuList = sysMenuService.queryMenuByUserIdAndApplicationName(userId,applicationName);
        // 通过userId和应用名获取用户权限
        Set<String> stringSet = sysRestService.getUrlByUserIdAndApplicationName(userId,applicationName);
        return Result.success().put("menuList", sysMenuList).put("permList", stringSet);
    }

}
