package com.gaoqisen.webcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.entity.SysUser;
import com.gaoqisen.webcenter.entity.SysUserRole;
import com.gaoqisen.webcenter.form.PasswordForm;
import com.gaoqisen.webcenter.service.SysUserRoleService;
import com.gaoqisen.webcenter.service.SysUserService;
import com.gaoqisen.webcenter.utils.CurrentPage;
import com.gaoqisen.webcenter.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @PostMapping("list")
    @ApiOperation("分页获取用户列表")
    public Result list(@RequestBody CurrentPage currentPage) {
        return Result.success().put("userList", this.sysUserService.queryPage(currentPage));
    }

    @ApiOperation("修改密码")
    @PostMapping("password")
    public Result password(@RequestBody PasswordForm passwordForm) {

        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //sha256加密
        String password = new Sha256Hash(passwordForm.getPassword(), sysUser.getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(passwordForm.getNewPassword(), sysUser.getSalt()).toHex();

        //更新密码
        boolean flag = sysUserService.updatePassword(sysUser.getUserId(), password, newPassword);
        if(!flag){
            return Result.error("原密码不正确");
        }


        return Result.success();
    }

    @PostMapping("save")
    @ApiOperation("新增用户")
    public Result save(@RequestBody SysUser sysUser){
        if(StringUtils.isBlank(sysUser.getUsername())) {
            return Result.error("用户名获取失败");
        }
        if(StringUtils.isBlank(sysUser.getPassword())) {
            return Result.error("密码不能为空");
        }
        sysUser.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        sysUser.setPassword(new Sha256Hash(sysUser.getPassword(), salt).toHex());
        sysUser.setSalt(salt);
        this.sysUserService.save(sysUser);
        return Result.success();
    }

    @ApiOperation("删除用户")
    @PostMapping("/delete")
    public Result delete(@RequestBody Long[] userIds) {
        if(userIds.length < 1) {
            return Result.error("ID不能为空");
        }
        if(ArrayUtils.contains(userIds, 1L)){
            return Result.error("系统管理员不能删除");
        }

        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (ArrayUtils.contains(userIds, sysUser.getUserId())) {
            return Result.error("当前用户不能删除");
        }
        this.sysUserService.removeByIds(Arrays.asList(userIds));
        this.sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", userIds));
        return Result.success();
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable String id) {
        if(StringUtils.isBlank(id)) {
            return Result.error("ID获取失败");
        }
        SysUser sysUser = this.sysUserService.getById(id);
        sysUser.setRoleIdList(this.sysUserRoleService.queryRoleIdList(id));
        return Result.success().put("sysUser", sysUser);
    }

    @PostMapping("/update")
    @ApiOperation("更新用户")
    public Result update(@RequestBody SysUser sysUser) {
        if(sysUser.getUserId() == null) {
            return Result.error("ID不能为空");
        }
        this.sysUserService.updateById(sysUser);
        return Result.success();
    }
}
