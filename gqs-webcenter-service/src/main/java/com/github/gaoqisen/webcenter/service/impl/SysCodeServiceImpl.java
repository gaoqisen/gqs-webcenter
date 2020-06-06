package com.github.gaoqisen.webcenter.service.impl;

import com.github.gaoqisen.webcenter.entity.SysCode;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.gaoqisen.webcenter.mapper.SysCodeMapper;
import com.github.gaoqisen.webcenter.service.SysCodeService;

import java.util.Map;

@Service
public class SysCodeServiceImpl extends ServiceImpl<SysCodeMapper, SysCode> implements SysCodeService {

	@Override
	public Map<String, String> sysStatisticsl() {
		return this.baseMapper.sysStatisticsl();
	}

}
