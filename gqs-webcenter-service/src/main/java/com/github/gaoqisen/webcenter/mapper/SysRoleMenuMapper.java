package com.github.gaoqisen.webcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.gaoqisen.webcenter.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

	List<Long> queryMenuIdByRoleIdAndApplicationName(Map<String, Object> param);

}