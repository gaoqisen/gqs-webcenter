package com.gaoqisen.webcenter.controller;

import com.gaoqisen.webcenter.service.SysConfigService;
import com.gaoqisen.webcenter.utils.CurrentPage;
import com.gaoqisen.webcenter.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping("list")
    @ApiOperation("获取系统配置")
    public Result list(@RequestBody CurrentPage gPage) {
        return Result.success().putData(this.sysConfigService.queryPage(gPage));
    }

}
