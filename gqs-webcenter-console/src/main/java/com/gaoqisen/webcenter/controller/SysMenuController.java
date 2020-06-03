package com.gaoqisen.webcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.constant.Constant;
import com.gaoqisen.webcenter.exception.AppException;
import com.gaoqisen.webcenter.service.*;
import com.gaoqisen.webcenter.utils.Result;
import com.gaoqisen.webcenter.entity.SysCodeMenu;
import com.gaoqisen.webcenter.entity.SysMenu;
import com.gaoqisen.webcenter.entity.SysRoleMenu;
import com.gaoqisen.webcenter.entity.SysUser;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRestService sysRestService;

    @Autowired
    private SysCodeMenuService sysCodeMenuService;

    @Autowired
    private SysCodeService sysCodeService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Value("${spring.application.name}")
    private String springApplicationName;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "nav")
    @ApiOperation("菜单信息获取")
    public Result navigation(HttpServletRequest httpServletRequest) {
        SysUser sysUser = getSysUserBySessionId(httpServletRequest.getSession().getId());
        if(sysUser == null) {
            // 未获取到用户信息，判断为其他系统登出，当前系统也登出
            SecurityUtils.getSubject().logout();
            return Result.success().put("menuList", new ArrayList()).put("permList", new Set[]{});
        }
        // 通userId和应用名获取菜单
        List<SysMenu> sysMenuList = sysMenuService.queryMenuByUserIdAndApplicationName(sysUser.getUserId(),springApplicationName);
        // 通过userId和应用名获取用户权限
        Set<String> stringSet = sysRestService.getUrlByUserIdAndApplicationName(sysUser.getUserId(),springApplicationName);
        return Result.success().put("menuList", sysMenuList).put("permList", stringSet);
    }

    private SysUser getSysUserBySessionId(String sessionId) {
        String redisLoginKey = springApplicationName.toUpperCase().concat(sessionId);
        String redisUserInfo = stringRedisTemplate.opsForValue().get(redisLoginKey);
        if(redisUserInfo == null || redisUserInfo.isEmpty()) {
            return null;
        }
        return JSONObject.parseObject(redisUserInfo, SysUser.class);
    }


    @ApiOperation("获取菜单列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Result list(@RequestParam(name = "applicationName", required = false) String applicationName) {
        if(StringUtils.isBlank(applicationName)) {
            applicationName = springApplicationName;
        }
        return Result.success().putData(sysMenuService.queryMenuByApplicationName(applicationName));
    }

    @ApiOperation("保存菜单")
    @PostMapping(value = "save")
    public Result save(@RequestBody SysMenu sysMenu) {
        verifyForm(sysMenu);
        if(StringUtils.isBlank(sysMenu.getSysCodeId())) {
            return Result.error("系统ID获取失败");
        }
        this.sysMenuService.save(sysMenu);
        SysCodeMenu sysCodeMenu = new SysCodeMenu();
        sysCodeMenu.setSysId(sysMenu.getSysCodeId());
        sysCodeMenu.setMenuId(sysMenu.getMenuId().toString());
        this.sysCodeMenuService.save(sysCodeMenu);
        return Result.success();
    }

    @ApiOperation("更新菜单")
    @PostMapping(value = "update")
    public Result update(@RequestBody SysMenu sysMenu) {
        if(sysMenu.getMenuId() == null) {
            return Result.error("菜单ID不能为空");
        }
        this.sysMenuService.updateById(sysMenu);
        return Result.success();
    }

    @ApiOperation("通过菜单ID获取菜单信息")
    @PostMapping(value = "/info/{menuId}")
    public Result info(@PathVariable String menuId) {
        if(menuId == null) {
            return Result.error("菜单ID不能为空");
        }
        SysMenu sysMenu = this.sysMenuService.getById(menuId);
        return Result.success().put("menu", sysMenu);
    }

    @ApiOperation("通过菜单ID删除菜单")
    @PostMapping(value = "/delete/{menuId}")
    public Result delete(@PathVariable Long menuId) {
        if(menuId == null){
            return Result.error("菜单ID不能为空");
        }
        // 判断是否有下级菜单
        Integer count = this.sysMenuService.count(new QueryWrapper<SysMenu>().eq(SysMenu.COL_PARENT_ID, menuId));
        if(count > 0) {
            return Result.error("请先删除子菜单或按钮");
        }
        this.sysMenuService.removeById(menuId);
        this.sysCodeMenuService.remove(new QueryWrapper<SysCodeMenu>().eq(SysMenu.COL_MENU_ID, menuId));
        this.sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq(SysMenu.COL_MENU_ID, menuId));
        return Result.success();
    }

    @ApiOperation("查询所有菜单")
    @GetMapping(value = "/queryMenu")
    public Result queryMenu(@RequestParam(name = "applicationName", required = false) String applicationName) {
        if(StringUtils.isBlank(applicationName)) {
            applicationName = springApplicationName;
        }
        List<SysMenu> menuList = this.sysMenuService.queryMenu(applicationName);
        return Result.success().putData(menuList);
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenu menu){
        if(StringUtils.isBlank(menu.getName())){
            throw new AppException("菜单名称不能为空");
        }

        if(menu.getParentId() == null){
            throw new AppException("上级菜单不能为空");
        }

        //菜单
        if(menu.getType() == Constant.MenuType.MENU.getValue()){
            if(StringUtils.isBlank(menu.getUrl())){
                throw new AppException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if(menu.getParentId() != 0){
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()){
            if(parentType != Constant.MenuType.CATALOG.getValue()){
                throw new AppException("上级菜单只能为目录类型");
            }
            return ;
        }
    }

}
