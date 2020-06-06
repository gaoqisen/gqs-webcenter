package com.github.gaoqisen.webcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.gaoqisen.webcenter.entity.SysRest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
import java.util.Set;

@Mapper
public interface SysRestMapper extends BaseMapper<SysRest> {

	Set<String> getPermissionsByUserId(Map<String, Object> param);

	Set<String> getUrlByUserIdAndApplicationName(Map<String, Object> param);

	Set<String> queryDigestByApplicationName(Map<String, Object> param);

}