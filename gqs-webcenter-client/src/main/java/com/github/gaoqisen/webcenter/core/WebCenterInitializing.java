package com.github.gaoqisen.webcenter.core;

import com.alibaba.fastjson.JSON;
import com.github.gaoqisen.webcenter.constant.RedisKeyConstant;
import com.github.gaoqisen.webcenter.constant.ServerContextConstant;
import com.github.gaoqisen.webcenter.http.HttpUtil;
import com.github.gaoqisen.webcenter.pojo.SysRest;
import com.github.gaoqisen.webcenter.pojo.WebCenterConsole;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebCenterInitializing implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("WebCenterInitializing init.");
        WebCenterConsole webCenterConsole = WebCenterClientBeanFactory.getBean(WebCenterConsole.class);
        RequestMappingHandlerMapping requestMappingHandlerMapping = WebCenterClientBeanFactory.getApplicationContext().getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        List<SysRest> restList = new ArrayList<SysRest>();
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
            sysRest.setApplicationName(webCenterConsole.getCurrentApplicationName());
            restList.add(sysRest);
        }
        stringRedisTemplate.opsForList().leftPush(RedisKeyConstant.RESTDATASET, JSON.toJSONString(restList));
        logger.info("redis list push success.");
    }
}
