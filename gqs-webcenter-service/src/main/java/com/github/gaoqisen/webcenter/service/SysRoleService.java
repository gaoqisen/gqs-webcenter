package com.github.gaoqisen.webcenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gaoqisen.webcenter.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.gaoqisen.webcenter.utils.CurrentPage;

public interface SysRoleService extends IService<SysRole> {

	IPage queryPage(CurrentPage currentPage);

}
