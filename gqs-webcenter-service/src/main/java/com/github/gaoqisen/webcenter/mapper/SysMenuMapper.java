package com.github.gaoqisen.webcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.gaoqisen.webcenter.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

	List<SysMenu> queryMenuByApplicationName(@Param("applicationName") String applicationName);

	List<SysMenu> queryMenuByUserIdAndApplicationNameAndParentId(Map<String, Object> param);

	List<SysMenu> queryMenu(@Param("applicationName") String applicationName);

}