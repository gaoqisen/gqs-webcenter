package com.gaoqisen.sample.config;


import com.github.gaoqisen.webcenter.api.ApiController;
import com.github.gaoqisen.webcenter.core.WebCenterClientBeanFactory;
import com.github.gaoqisen.webcenter.core.WebCenterInitializing;
import com.github.gaoqisen.webcenter.pojo.WebCenterConsole;
import com.github.gaoqisen.webcenter.shiro.MyShiroRealm;
import com.github.gaoqisen.webcenter.shiro.ShiroConfigUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class WebCenterConfig {


    @Value("${webcenter.server.host}")
    private String host;

    @Value("${webcenter.server.port}")
    private String port;

    @Value("${webcenter.server.clientid}")
    private String clientId;

    @Value("${webcenter.server.secretkey}")
    private String secretKey;

    @Value("${spring.application.name}")
    private String applicationName;


    @Bean
    @DependsOn("webCenterConsole")
    public WebCenterClientBeanFactory springClientBeanFactory() {
        return new WebCenterClientBeanFactory();
    }

    @Bean
    @DependsOn("springClientBeanFactory")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager defaultWebSecurityManager) {
        return new ShiroConfigUtils().getShiroFilter(defaultWebSecurityManager);
    }

    @Bean
    public WebCenterInitializing webCenterInitializing() {
        return new WebCenterInitializing();
    }

    @Bean
    public WebCenterConsole webCenterConsole(){
        WebCenterConsole webCenterConsole = new WebCenterConsole();
        webCenterConsole.setHost(host);
        webCenterConsole.setPort(port);
        webCenterConsole.setClientId(clientId);
        webCenterConsole.setSecretKey(secretKey);
        webCenterConsole.setCurrentApplicationName(applicationName);
        return webCenterConsole;
    }

    @Bean
    public ApiController apiController() {
        return new ApiController();
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }


    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
