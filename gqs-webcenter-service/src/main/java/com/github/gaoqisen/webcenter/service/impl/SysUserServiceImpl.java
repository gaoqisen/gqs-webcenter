package com.github.gaoqisen.webcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gaoqisen.webcenter.entity.SysUser;
import com.github.gaoqisen.webcenter.utils.CurrentPage;
import com.github.gaoqisen.webcenter.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.gaoqisen.webcenter.mapper.SysUserMapper;

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
