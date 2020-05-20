package com.gaoqisen.webcenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaoqisen.webcenter.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gaoqisen.webcenter.utils.CurrentPage;

public interface SysConfigService extends IService<SysConfig>{

    IPage<SysConfig> queryPage(CurrentPage gPage);


}
