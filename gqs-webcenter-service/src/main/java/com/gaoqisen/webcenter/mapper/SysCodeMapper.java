package com.gaoqisen.webcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaoqisen.webcenter.entity.SysCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface SysCodeMapper extends BaseMapper<SysCode> {

    Map<String, String> sysStatisticsl();

}