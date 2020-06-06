package com.github.gaoqisen.webcenter.service;

import com.github.gaoqisen.webcenter.entity.SysCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface SysCodeService extends IService<SysCode> {

	Map<String, String> sysStatisticsl();

}
