package com.ali.speech.core.env;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 全局可访问appContext，用于解决websocket无法注入对象的问题
 */
@Component
public class LocalAppContext implements ApplicationContextAware {

    public static ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LocalAppContext.appContext = applicationContext;
    }
}
