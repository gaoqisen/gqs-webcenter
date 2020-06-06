package com.github.gaoqisen.webcenter.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.github.gaoqisen.webcenter.service.SysLogService;
import com.github.gaoqisen.webcenter.utils.CurrentPage;
import com.github.gaoqisen.webcenter.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sys/log")
public class SysLogController {

    private static Logger logger = LoggerFactory.getLogger(SysLogController.class);

    @Autowired
    private SysLogService sysLogService;

    @PostMapping("list")
    @ApiOperation("分页获取日志")
    public Result list(@RequestBody CurrentPage page) {
        return Result.success().put("logList", this.sysLogService.listPage(page));
    }

    @RequestMapping(value = "changeLevel")
    @ApiOperation("更改日志级别")
    public String changeLevel(String allLevel,String singleLevel,String singlePath) {
        LoggerContext loggerContext= (LoggerContext) LoggerFactory.getILoggerFactory();
        if(!StringUtils.isEmpty(allLevel)){
            //设置全局日志级别
            ch.qos.logback.classic.Logger logger=loggerContext.getLogger("root");
            logger.setLevel(Level.toLevel(allLevel));
        }

        if (!StringUtils.isEmpty(singleLevel)) {
            //设置某个类日志级别-可以实现定向日志级别调整
            ch.qos.logback.classic.Logger vLogger = loggerContext.getLogger(singlePath);
            if (vLogger!=null){
                vLogger.setLevel(Level.toLevel(singleLevel));
            }
        }
        return "success";
    }
}
