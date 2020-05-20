package com.gaoqisen.webcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gaoqisen.webcenter.utils.CurrentPage;
import com.gaoqisen.webcenter.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gaoqisen.webcenter.mapper.SysUserMapper;
import com.gaoqisen.webcenter.entity.SysUser;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public IPage<SysUser> queryPage(CurrentPage currentPage) {
        return this.page(currentPage.getPage(), new QueryWrapper<SysUser>()
                .like(StringUtils.isNotBlank(currentPage.getParams()), "username", currentPage.getParams()));
    }

    @Override
    public boolean updatePassword(Integer userId, String password, String newPassword) {
        SysUser userEntity = new SysUser();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<SysUser>().eq("user_id", userId).eq("password", password));
    }
}
