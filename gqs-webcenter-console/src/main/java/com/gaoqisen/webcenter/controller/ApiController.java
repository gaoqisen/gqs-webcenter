package com.gaoqisen.webcenter.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gaoqisen.webcenter.entity.SysMenu;
import com.gaoqisen.webcenter.entity.SysRest;
import com.gaoqisen.webcenter.entity.SysUser;
import com.gaoqisen.webcenter.service.SysMenuService;
import com.gaoqisen.webcenter.service.SysRestService;
import com.gaoqisen.webcenter.service.SysUserService;
import com.gaoqisen.webcenter.utils.DigestUtils;
import com.gaoqisen.webcenter.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SysRestService sysRestService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @ApiOperation("保存rest接口列表")
    @RequestMapping("/sys/rest/saves")
    public Result saveSysRestList(@RequestBody List<SysRest> restList) {
        if (restList == null) {
            return Result.error("接口类别获取失败");
        }
        for(SysRest sysRest : restList) {
            sysRest.setCreateTime(new Date());
            sysRest.setDigest(DigestUtils.getDigest(sysRest.getApplicationName().concat(sysRest.getUrl())));
            this.sysRestService.saveOrUpdate(sysRest);
        }
        String currentApplicationName = restList.get(0).getApplicationName();
        String currentApplicationRest = stringRedisTemplate.opsForValue().get(currentApplicationName);
        if(currentApplicationRest == null || currentApplicationRest.isEmpty()) {
            List<SysRest> list = this.sysRestService.list(new QueryWrapper<SysRest>().eq("application_name",currentApplicationName));
            stringRedisTemplate.opsForValue().set(currentApplicationName, JSON.toJSONString(list));
        }
        return Result.success();
    }

}
