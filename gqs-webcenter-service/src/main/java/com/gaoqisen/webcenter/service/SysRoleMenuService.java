package com.gaoqisen.webcenter.service;

import com.gaoqisen.webcenter.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleMenuService extends IService<SysRoleMenu>{

    List<Long> queryMenuIdByRoleIdAndApplicationName(Long roleId, String applicationName);


}
