package com.github.gaoqisen.webcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gaoqisen.webcenter.entity.SysMenu;
import com.github.gaoqisen.webcenter.exception.AppException;
import com.github.gaoqisen.webcenter.service.SysMenuService;
import com.github.gaoqisen.webcenter.utils.CurrentPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.gaoqisen.webcenter.mapper.SysMenuMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public IPage<SysMenu> queryList(CurrentPage currentPage) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(currentPage.getParams()),"name", currentPage.getParams());
        return this.page(currentPage.getPage(), queryWrapper);
    }

    @Override
    public List<SysMenu> queryMenuByApplicationName(String applicationName) {
        if(StringUtils.isBlank(applicationName)) {
            throw new AppException("应用名称获取失败");
        }
        return this.baseMapper.queryMenuByApplicationName(applicationName);
    }

    @Override
    public List<SysMenu> queryMenuByUserIdAndApplicationNameAndParentId(Integer userId, String applicationName, Long parentId) {
        if(userId == null) {
            throw new AppException("用户ID获取失败");
        }
        if(StringUtils.isBlank(applicationName)) {
            throw new AppException("应用名称获取失败");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("applicationName", applicationName);
        param.put("parentId", parentId);
        return this.baseMapper.queryMenuByUserIdAndApplicationNameAndParentId(param);
    }

    @Override
    public List<SysMenu> queryMenuByUserIdAndApplicationName(Integer userId, String applicationName) {
        // 获取主菜单
        List<SysMenu> menuList = queryMenuByUserIdAndApplicationNameAndParentId(userId, applicationName, 0L);
        // 通过主菜单递归获取所有菜单
        List<SysMenu> sysMenuList = getMenuTreeList(menuList, userId, applicationName);

        return sysMenuList;
    }

    @Override
    public List<SysMenu> queryMenu(String applicationName) {
        List<SysMenu> menuList = this.baseMapper.queryMenu(applicationName);
        SysMenu sysMenu = new SysMenu();
        sysMenu.setName("一级菜单");
        sysMenu.setParentId(0L);
        sysMenu.setMenuId(0L);
        menuList.add(sysMenu);
        return menuList;
    }

    private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, Integer userId, String applicationName) {
        List<SysMenu> sysMenus = new ArrayList<SysMenu>();

        for (int i = 0; i < menuList.size(); i++) {
            SysMenu sysMenu = menuList.get(i);
            if(sysMenu.getType() == 0){
                sysMenu.setList(getMenuTreeList(queryMenuByUserIdAndApplicationNameAndParentId(userId, applicationName, sysMenu.getMenuId()),userId, applicationName));
            }
            sysMenus.add(sysMenu);
        }
        return menuList;

    }

}
