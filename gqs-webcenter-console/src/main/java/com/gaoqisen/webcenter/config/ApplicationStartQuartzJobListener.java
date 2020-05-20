package com.gaoqisen.webcenter.config;

import com.gaoqisen.webcenter.entity.SysRest;
import com.gaoqisen.webcenter.service.SysRestService;
import com.gaoqisen.webcenter.utils.DigestUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.*;


@Component
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    private SysRestService sysRestService;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodEntry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = mappingInfoHandlerMethodEntry.getKey();
            PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
            String restUrl = patternsCondition.getPatterns().iterator().next();
            // 过滤掉error和swagger路径
            if(restUrl.startsWith("/error") || restUrl.startsWith("/swagger")){
                continue;
            }
            SysRest sysRest = new SysRest();
            HandlerMethod handlerMethod = mappingInfoHandlerMethodEntry.getValue();
            Annotation[] annotations = handlerMethod.getMethod().getDeclaredAnnotations();
            if (annotations != null) {
                // 处理具体的方法信息
                for (Annotation annotation : annotations) {
                    if (annotation instanceof ApiOperation) {
                        ApiOperation methodDesc = (ApiOperation) annotation;
                        //接口描述
                        sysRest.setApiName(methodDesc.value());
                    }
                }
            }
            sysRest.setUrl(restUrl);
            sysRest.setApplicationName(applicationName);
            sysRest.setCreateTime(new Date());
            sysRest.setDigest(DigestUtils.getDigest(applicationName.concat(restUrl)));
            sysRestService.saveOrUpdate(sysRest);
        }
    }
}
