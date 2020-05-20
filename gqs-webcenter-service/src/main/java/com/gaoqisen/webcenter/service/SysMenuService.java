package com.gaoqisen.webcenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaoqisen.webcenter.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gaoqisen.webcenter.utils.CurrentPage;

import java.util.List;

public interface SysMenuService extends IService<SysMenu>{

    // 分页获取菜单
    IPage<SysMenu> queryList(CurrentPage currentPage);

    List<SysMenu> queryMenuByApplicationName(String applicationName);

    List<SysMenu> queryMenuByUserIdAndApplicationNameAndParentId(Integer userId, String applicationName, Long parentId);

    List<SysMenu> queryMenuByUserIdAndApplicationName(Integer userId, String applicationName);

    List<SysMenu> queryMenu(String applicationName);
}
