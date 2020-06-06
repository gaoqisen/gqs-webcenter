package com.github.gaoqisen.webcenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gaoqisen.webcenter.entity.SysRest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.gaoqisen.webcenter.utils.CurrentPage;

import java.util.Set;

public interface SysRestService extends IService<SysRest> {

	Set<String> getPermissionsByUserIdAndApplicationName(Integer userId, String applicationName);

	Set<String> getUrlByUserIdAndApplicationName(Integer userId, String applicationName);

	IPage<SysRest> queryPage(CurrentPage gPage);

	Set<String> queryDigestByApplicationName(String applicationName, Long roleId);

}
