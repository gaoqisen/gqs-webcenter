package com.github.gaoqisen.webcenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gaoqisen.webcenter.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.gaoqisen.webcenter.utils.CurrentPage;

public interface SysUserService extends IService<SysUser> {

	IPage<SysUser> queryPage(CurrentPage currentPage);

	boolean updatePassword(Integer userId, String password, String newPassword);

}
