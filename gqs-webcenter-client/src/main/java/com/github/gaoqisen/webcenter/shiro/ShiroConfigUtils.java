package com.github.gaoqisen.webcenter.shiro;


import com.github.gaoqisen.webcenter.constant.ServerContextConstant;
import com.github.gaoqisen.webcenter.http.HttpUtil;
import com.github.gaoqisen.webcenter.pojo.SysRest;
import com.google.common.collect.Maps;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShiroConfigUtils {

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 设置过滤规则
     *
     * @param securityManager
     * @return
     */
    public ShiroFilterFactoryBean getShiroFilter(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //获取filters
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();

        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        // TODO 用application_name通过http获取所有配置权限
        List<SysRest> list = HttpUtil.parseArray(ServerContextConstant.API_USER_INFO, Maps.newHashMap(), SysRest.class);
        for(SysRest obj : list) {
            filterChainDefinitionMap.put(obj.getUrl(), obj.getPermissions());
        }

        filters.put("authc", new MyFormAuthenticationFilter());
        filters.put("perms", new MyPermissionsAuthorizationFilter());

        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

}
