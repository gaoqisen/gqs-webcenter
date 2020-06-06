package com.github.gaoqisen.webcenter.service;

import com.github.gaoqisen.webcenter.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {

	List<Long> queryRoleIdList(String userId);

}
