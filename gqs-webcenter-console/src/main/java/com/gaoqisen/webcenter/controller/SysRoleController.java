package com.gaoqisen.webcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.constant.RedisKeyConstant;
import com.gaoqisen.webcenter.entity.*;
import com.gaoqisen.webcenter.service.*;
import com.gaoqisen.webcenter.utils.CurrentPage;
import com.gaoqisen.webcenter.utils.Result;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRestService sysRestService;

    @Autowired
    private SysCodeService sysCodeService;

    @Autowired
    private SysRoleRestService sysRoleRestService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 角色list
     */
    private final static String ROLE_LIST = "roleList";

    @ApiOperation("获取全部角色")
    @GetMapping("queryAll")
    public Result queryAll(){
        return Result.success().put(ROLE_LIST, this.sysRoleService.list());
    }

    @ApiOperation("分页查询角色")
    @PostMapping("list")
    public Result list(@RequestBody CurrentPage currentPage) {
        return Result.success().put(ROLE_LIST, this.sysRoleService.queryPage(currentPage));
    }

    @ApiOperation("增加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole) {
        if(StringUtils.isBlank(sysRole.getRoleName())) {
            return Result.error("角色名称不能为空");
        }
        sysRole.setCreateTime(new Date());
        this.sysRoleService.save(sysRole);
        HashMap roleLimitInfo = sysRole.getRoleLimitInfo();

        for (Object obj : roleLimitInfo.values()) {
            HashMap hashMap = (HashMap)obj;
            // 菜单更新
            List<Integer> menus = (List<Integer>) hashMap.get(MENU_CHECK);
            if(menus != null){
                for(Integer menuId : menus){
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(sysRole.getRoleId());
                    sysRoleMenu.setMenuId(Long.valueOf(menuId));
                    this.sysRoleMenuService.save(sysRoleMenu);
                }
            }

            // REST接口更新
            List<String> rests = (List<String>) hashMap.get(REST_CHECK);
            if(rests != null) {
                for(String digest : rests) {
                    SysRoleRest sysRoleRest = new SysRoleRest();
                    sysRoleRest.setRestId(digest);
                    sysRoleRest.setRoleId(sysRole.getRoleId());
                    this.sysRoleRestService.save(sysRoleRest);
                }
            }
        }
        logger.info(sysRole.getRoleName().concat("添加成功."));
        return Result.success();
    }

    @ApiOperation("删除角色")
    @PostMapping("delete")
    public Result delete(@RequestBody Long[] ids) {
        if(ids.length < 1) {
            return Result.error("ID不能为空");
        }

        Integer count = this.sysUserRoleService.count(new QueryWrapper<SysUserRole>().in(SysUserRole.COL_ROLE_ID, ids));
        if (count > 0) {
            return Result.error("无法删除在用角色");
        }

        this.sysRoleService.removeByIds(Arrays.asList(ids));
        for (int i = 0; i < ids.length; i++) {
            // 删除角色菜单权限
            this.sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq(SysUserRole.COL_ROLE_ID,ids[i]));
            // 删除角色rest权限
            this.sysRoleRestService.remove(new QueryWrapper<SysRoleRest>().eq(SysUserRole.COL_ROLE_ID, ids[i]));
        }
        return Result.success();
    }

    /**
     * 选中的菜单
     */
    private final static String MENU_CHECK = "menuCheck";


    /**
     * 选中的接口
     */
    private final static String REST_CHECK = "restCheck";

    @PostMapping("update")
    @ApiOperation("修改角色")
    public Result update(@RequestBody SysRole sysRole) {
        if(sysRole.getRoleId() == null) {
            return Result.error("ID不能为空");
        }
        this.sysRoleService.updateById(sysRole);

        this.sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq(SysUserRole.COL_ROLE_ID,sysRole.getRoleId()));
        this.sysRoleRestService.remove(new QueryWrapper<SysRoleRest>().eq(SysUserRole.COL_ROLE_ID, sysRole.getRoleId()));

        HashMap roleLimitInfo = sysRole.getRoleLimitInfo();

        for (Object obj : roleLimitInfo.values()) {
            HashMap hashMap = (HashMap)obj;
            // 菜单更新
            List<Integer> menus = (List<Integer>) hashMap.get(MENU_CHECK);
            if(menus != null){
                for(Integer menuId : menus){
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(sysRole.getRoleId());
                    sysRoleMenu.setMenuId(Long.valueOf(menuId));
                    this.sysRoleMenuService.save(sysRoleMenu);
                }
            }

            // REST接口更新
            List<String> rests = (List<String>) hashMap.get(REST_CHECK);
            if(rests != null && rests.size() > 0) {
                for(String digest : rests) {
                    SysRoleRest sysRoleRest = new SysRoleRest();
                    sysRoleRest.setRestId(digest);
                    sysRoleRest.setRoleId(sysRole.getRoleId());
                    this.sysRoleRestService.save(sysRoleRest);
                }
            }
        }

        // 通过角色ID获取userId并退出所有登录的用户，实现修改权限立即生效的功能
        List<SysUserRole> list = this.sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq(SysUserRole.COL_ROLE_ID, sysRole.getRoleId()));
        for(SysUserRole userRole : list) {
            String loginUserKey = RedisKeyConstant.USERLOGINSET.concat(String.valueOf(userRole.getUserId()));
            Set<String> redisUsers = stringRedisTemplate.opsForSet().members(loginUserKey);
            if(redisUsers != null && redisUsers.size() > 0) {
                for(String redisKey : redisUsers) {
                    stringRedisTemplate.delete(redisKey);
                }
                stringRedisTemplate.delete(loginUserKey);
            }

        }
        logger.info(sysRole.getRoleName().concat("修改成功，角色下所有账号清除登录信息，需重新登录."));
        return Result.success();
    }

    /**
     * 角色信息
     */
    private final static String SYS_ROLE = "sysRole";

    @GetMapping("/info/{roleId}")
    @ApiOperation("通过ID获取角色信息")
    public Result info(@PathVariable Long roleId) {
        if(roleId == null) {
            return Result.error("ID不能为空");
        }
        SysRole sysRole = this.sysRoleService.getById(roleId);
        // 封装角色信息
        List<SysCode> sysCodes = this.sysCodeService.list();
        HashMap<Object, Object> roleSysInfo = Maps.newHashMap();
        for (SysCode sysCode : sysCodes) {
            HashMap data = Maps.newHashMap();
            data.put(MENU_CHECK, this.sysRoleMenuService.queryMenuIdByRoleIdAndApplicationName(sysRole.getRoleId(), sysCode.getApplicationName()));
            data.put(REST_CHECK, this.sysRestService.queryDigestByApplicationName(sysCode.getApplicationName(), sysRole.getRoleId()));
            roleSysInfo.put(sysCode.getApplicationName(), data);
        }
        sysRole.setRoleLimitInfo(roleSysInfo);
        return Result.success().put(SYS_ROLE, sysRole);
    }

}
