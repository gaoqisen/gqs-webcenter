package com.github.gaoqisen.webcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gaoqisen.webcenter.entity.SysRole;
import com.github.gaoqisen.webcenter.utils.CurrentPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.gaoqisen.webcenter.mapper.SysRoleMapper;
import com.github.gaoqisen.webcenter.service.SysRoleService;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	@Override
	public IPage queryPage(CurrentPage currentPage) {
		return this.page(currentPage.getPage(), new QueryWrapper<SysRole>()
				.like(StringUtils.isNotBlank(currentPage.getParams()), "role_name", currentPage.getParams()));
	}

}
