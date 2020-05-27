package com.gaoqisen.webcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gaoqisen.webcenter.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    List<Map<String, String>> statisticalUserLogin(Map<String, String> param);

}