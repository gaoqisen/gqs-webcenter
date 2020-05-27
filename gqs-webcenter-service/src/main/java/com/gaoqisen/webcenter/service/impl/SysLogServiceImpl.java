package com.gaoqisen.webcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaoqisen.webcenter.exception.AppException;
import com.gaoqisen.webcenter.utils.CurrentPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gaoqisen.webcenter.entity.SysLog;
import com.gaoqisen.webcenter.mapper.SysLogMapper;
import com.gaoqisen.webcenter.service.SysLogService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService{

    @Override
    public IPage<SysLog> listPage(CurrentPage page) {
        IPage<SysLog> sysLogIPage = this.page(page.getPage(), new QueryWrapper<SysLog>().eq(StringUtils.isNotBlank(page.getParams()), "username", page.getParams()));
        return sysLogIPage;
    }

    @Override
    public List<Map<String, String>> statisticalUserLogin(Map<String, String> param) {

        String dateFormat = param.get("dateFormat");

        if(!Arrays.asList(new String[]{"1","2","3"}).contains(dateFormat)){
            throw new AppException("时间格式获取失败");
        }

        if("1".equals(dateFormat)){
            param.put("dateFormat", "%Y-%m-%d");
        }
        if("2".equals(dateFormat)) {
            param.put("dateFormat", "%Y-%m");
        }
        if("3".equals(dateFormat)) {
            param.put("dateFormat", "%Y");
        }

        if(StringUtils.isBlank(param.get("startTime"))) {
            throw new AppException("开始时间获取失败");
        }

        if(StringUtils.isBlank(param.get("endTime"))) {
            throw new AppException("结束时间获取失败");
        }

        return this.baseMapper.statisticalUserLogin(param);
    }
}
