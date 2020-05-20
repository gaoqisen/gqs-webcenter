package com.github.gaoqisen.webcenter.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class WebCenterClientBeanFactory implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(getClass());

    public WebCenterClientBeanFactory() {
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("webCenterClientBeanFactory init.");
        WebCenterClientBeanFactory.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> bean) {
        return applicationContext.getBean(bean);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
