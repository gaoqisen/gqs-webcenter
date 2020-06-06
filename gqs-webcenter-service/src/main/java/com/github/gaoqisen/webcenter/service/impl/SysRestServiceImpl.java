package com.github.gaoqisen.webcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gaoqisen.webcenter.entity.SysRest;
import com.github.gaoqisen.webcenter.exception.AppException;
import com.github.gaoqisen.webcenter.service.SysRestService;
import com.github.gaoqisen.webcenter.utils.CurrentPage;
import com.github.gaoqisen.webcenter.mapper.SysRestMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class SysRestServiceImpl extends ServiceImpl<SysRestMapper, SysRest> implements SysRestService {

    @Override
    public Set<String> getPermissionsByUserIdAndApplicationName(Integer userId, String applicationName) {
        if(userId == null) {
            throw new AppException("角色ID获取失败");
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("applicationName", applicationName);
        return this.baseMapper.getPermissionsByUserId(param);
    }

    @Override
    public Set<String> getUrlByUserIdAndApplicationName(Integer userId, String applicationName) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("applicationName", applicationName);
        Set<String> stringSet = this.baseMapper.getUrlByUserIdAndApplicationName(param);
        // 替换url为权限数据
        Set<String> hashSet = new HashSet<String>();
        for(String url : stringSet) {
            String urlSplit = url.replace("/", ":").replace(":{", "&").split("&")[0];
            hashSet.add(urlSplit.substring(1, urlSplit.length()));
        }
        return hashSet;
    }

    @Override
    public IPage<SysRest> queryPage(CurrentPage gPage) {
        if (StringUtils.isBlank(gPage.getParams())) {
            throw new AppException("应用名不能为空");
        }
        IPage<SysRest> sysRestIPage = this.page(gPage.getPage(), new QueryWrapper<SysRest>().eq("application_name", gPage.getParams()));
        return sysRestIPage;
    }

    @Override
    public Set<String> queryDigestByApplicationName(String applicationName, Long roleId) {
        Map<String, Object> param = new HashMap<>();
        param.put("applicationName", applicationName);
        param.put("roleId", roleId);
        return this.baseMapper.queryDigestByApplicationName(param);
    }
}

