package com.github.gaoqisen.webcenter.form;

import com.github.gaoqisen.webcenter.entity.SysCode;
import com.github.gaoqisen.webcenter.entity.SysRest;

import java.util.List;

public class SysFrom {

	private List<SysRest> restList;

	private SysCode sysCode;

	public SysCode getSysCode() {
		return sysCode;
	}

	public void setSysCode(SysCode sysCode) {
		this.sysCode = sysCode;
	}

	public List<SysRest> getRestList() {
		return restList;
	}

	public void setRestList(List<SysRest> restList) {
		this.restList = restList;
	}

}
