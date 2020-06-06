package com.github.gaoqisen.webcenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gaoqisen.webcenter.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.gaoqisen.webcenter.utils.CurrentPage;

import java.util.List;
import java.util.Map;

public interface SysLogService extends IService<SysLog> {

	IPage<SysLog> listPage(CurrentPage page);

	List<Map<String, String>> statisticalUserLogin(Map<String, String> param);

}
