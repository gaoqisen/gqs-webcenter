package com.github.gaoqisen.webcenter.service.impl;

import com.github.gaoqisen.webcenter.entity.SysRoleMenu;
import com.github.gaoqisen.webcenter.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.gaoqisen.webcenter.mapper.SysRoleMenuMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Override
    public List<Long> queryMenuIdByRoleIdAndApplicationName(Long roleId, String applicationName) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleId", roleId);
        param.put("applicationName", applicationName);
        return this.baseMapper.queryMenuIdByRoleIdAndApplicationName(param);
    }
}
