package com.gaoqisen.webcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaoqisen.webcenter.utils.CurrentPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gaoqisen.webcenter.entity.SysConfig;
import com.gaoqisen.webcenter.mapper.SysConfigMapper;
import com.gaoqisen.webcenter.service.SysConfigService;

@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService{

    @Override
    public IPage<SysConfig> queryPage(CurrentPage currentPage) {

        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(currentPage.getParams()),
                "param_key", currentPage.getParams()).eq("status", "1");
        IPage<SysConfig> page = this.page(currentPage.getPage(), queryWrapper);

        return page;
    }
}
