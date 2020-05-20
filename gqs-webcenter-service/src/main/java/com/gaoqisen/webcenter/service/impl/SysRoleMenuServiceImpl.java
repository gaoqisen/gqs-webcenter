package com.gaoqisen.webcenter.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gaoqisen.webcenter.mapper.SysRoleMenuMapper;
import com.gaoqisen.webcenter.entity.SysRoleMenu;
import com.gaoqisen.webcenter.service.SysRoleMenuService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService{

    @Override
    public List<Long> queryMenuIdByRoleIdAndApplicationName(Long roleId, String applicationName) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleId", roleId);
        param.put("applicationName", applicationName);
        return this.baseMapper.queryMenuIdByRoleIdAndApplicationName(param);
    }
}
