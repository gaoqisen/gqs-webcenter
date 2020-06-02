package com.gaoqisen.sample.controller;

import com.github.gaoqisen.webcenter.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {


    @ApiOperation("测试接口")
    @PostMapping("test")
    public void test() {
        System.out.println("test");
    }

}
