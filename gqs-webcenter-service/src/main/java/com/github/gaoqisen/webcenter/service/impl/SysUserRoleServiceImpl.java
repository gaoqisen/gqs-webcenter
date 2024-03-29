package com.github.gaoqisen.webcenter.service.impl;

import com.github.gaoqisen.webcenter.exception.AppException;
import com.github.gaoqisen.webcenter.service.SysUserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.gaoqisen.webcenter.mapper.SysUserRoleMapper;
import com.github.gaoqisen.webcenter.entity.SysUserRole;

import java.util.List;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

	@Override
	public List<Long> queryRoleIdList(String userId) {
		if (StringUtils.isBlank(userId)) {
			throw new AppException("用户ID获取失败");
		}
		return this.baseMapper.queryRoleIdList(userId);
	}

}
