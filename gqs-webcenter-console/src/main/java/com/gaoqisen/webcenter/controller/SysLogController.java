package com.gaoqisen.webcenter.controller;

import com.gaoqisen.webcenter.service.SysLogService;
import com.gaoqisen.webcenter.utils.CurrentPage;
import com.gaoqisen.webcenter.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @PostMapping("list")
    @ApiOperation("分页获取日志")
    public Result list(@RequestBody CurrentPage page) {
        return Result.success().put("logList", this.sysLogService.listPage(page));
    }

}
