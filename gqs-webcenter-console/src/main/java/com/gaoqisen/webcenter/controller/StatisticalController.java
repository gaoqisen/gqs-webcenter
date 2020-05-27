package com.gaoqisen.webcenter.controller;

import com.gaoqisen.webcenter.service.SysCodeService;
import com.gaoqisen.webcenter.service.SysLogService;
import com.gaoqisen.webcenter.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sys/statistical")
public class StatisticalController {

    @Autowired
    private SysCodeService sysCodeService;

    @Autowired
    private SysLogService sysLogService;

    @GetMapping("number")
    @ApiOperation("数量统计")
    public Result sysStatistical() {
        return Result.success().putData(this.sysCodeService.sysStatisticsl());
    }

    @PostMapping("login")
    @ApiOperation("统计登录数")
    public Result statisticalLoginNumber(@RequestBody Map<String, String> param) {
        return Result.success().putData(this.sysLogService.statisticalUserLogin(param));
    }

}
