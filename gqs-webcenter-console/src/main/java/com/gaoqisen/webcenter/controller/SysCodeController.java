package com.gaoqisen.webcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.entity.SysCode;
import com.gaoqisen.webcenter.entity.SysRest;
import com.gaoqisen.webcenter.exception.AppException;
import com.gaoqisen.webcenter.service.SysCodeService;
import com.gaoqisen.webcenter.service.SysMenuService;
import com.gaoqisen.webcenter.service.SysRestService;
import com.gaoqisen.webcenter.utils.Result;
import com.gaoqisen.webcenter.utils.SecretKeyUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/sys/code")
public class SysCodeController {

    @Autowired
    private SysCodeService sysCodeService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRestService sysRestService;

    @ApiOperation("获取所有系统信息")
    @GetMapping("queryAll")
    public Result queryAll() {
        List<SysCode> list = this.sysCodeService.list();
        return Result.success().putData(list);
    }

    @ApiOperation("获取所有系统信息")
    @GetMapping("queryAllInfo")
    public Result queryAllInfo() {
        List<SysCode> list = this.sysCodeService.list();
        HashMap dataList = Maps.newHashMap();
        for(SysCode sysCode : list) {
            HashMap hashMap = Maps.newHashMap();
            hashMap.put("menuList", this.sysMenuService.queryMenuByApplicationName(sysCode.getApplicationName()));
            hashMap.put("restList",  this.sysRestService.list(new QueryWrapper<SysRest>().eq(SysCode.COL_APPLICATION_NAME, sysCode.getApplicationName())));
            dataList.put(sysCode.getApplicationName(), hashMap);
        }
        return Result.success().put("sysCodeList",list).put("dataList", dataList);
    }

    @ApiOperation("通过ID获取系统信息")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable String id) {
        if(StringUtils.isBlank(id)) {
            return Result.error("ID获取失败");
        }
        return Result.success().put("sysCode", this.sysCodeService.getById(id));
    }

    @ApiOperation("系统信息删除")
    @GetMapping("delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        if(StringUtils.isBlank(id)) {
            return Result.error("ID获取失败");
        }
        this.sysCodeService.removeById(id);
        return Result.success();
    }

    @ApiOperation("系统信息修改")
    @PostMapping("update")
    public Result update(@RequestBody SysCode sysCode){
        if(sysCode.getId() == null) {
            return Result.error("ID获取失败");
        }
        this.sysCodeService.updateById(sysCode);
        return Result.success();
    }

    @ApiOperation("系统信息保存")
    @PostMapping("save")
    public Result save(@RequestBody SysCode sysCode) {
        verifySysCode(sysCode);
        sysCode.setCreateTime(new Date());
        sysCode.setSecretKey(SecretKeyUtils.getKeyCreate());
        sysCode.setClientId(SecretKeyUtils.getClientId());
        this.sysCodeService.save(sysCode);
        return Result.success();
    }
    private void verifySysCode(SysCode sysCode) {
        if(sysCode.getApplicationName() == null) {
            throw new AppException("应用名称不能为空");
        }
        if(sysCode.getName() == null) {
            throw new AppException("客户端名称不能为空");
        }
    }
}
