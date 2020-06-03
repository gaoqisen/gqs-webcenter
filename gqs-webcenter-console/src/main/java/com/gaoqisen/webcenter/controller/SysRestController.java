package com.gaoqisen.webcenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.entity.SysRest;
import com.gaoqisen.webcenter.service.SysRestService;
import com.gaoqisen.webcenter.utils.CurrentPage;
import com.gaoqisen.webcenter.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/rest")
public class SysRestController {

    @Autowired
    private SysRestService sysRestService;

    @Value("${spring.application.name}")
    private String springApplicationName;

    @ApiOperation("获取rest接口对象")
    @GetMapping("listRestByApplicationName")
    public  Result listObject(@RequestParam(name = "applicationName", required = false) String applicationName) {
        if(StringUtils.isBlank(applicationName)) {
            applicationName = springApplicationName;
        }
        return Result.success().put("listObject", this.sysRestService.list(new QueryWrapper<SysRest>().eq(SysRest.COL_APPLICATION_NAME, applicationName)));
    }

    @ApiOperation("分页获取REST接口信息")
    @PostMapping("list")
    public Result list(@RequestBody CurrentPage gPage) {
        return Result.success().put("restList", this.sysRestService.queryPage(gPage));
    }

    @ApiOperation("REST接口修改")
    @PostMapping("update")
    public Result update(@RequestBody SysRest sysRest) {
        if(StringUtils.isBlank(sysRest.getDigest())){
            return Result.error("摘要不能为空");
        }
        this.sysRestService.updateById(sysRest);
        return Result.success();
    }

}
