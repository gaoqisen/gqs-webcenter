package com.gaoqisen.webcenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaoqisen.webcenter.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gaoqisen.webcenter.utils.CurrentPage;

public interface SysUserService extends IService<SysUser>{


    IPage<SysUser> queryPage(CurrentPage currentPage);

    boolean updatePassword(Integer userId, String password, String newPassword);
}
