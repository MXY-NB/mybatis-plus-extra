package com.iv.ersr.common.util;

import cn.hutool.core.bean.BeanException;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Spring Bean获取工具类
 * </p>
 *
 * @author moxiaoyu
 * @since 2021-12-24
 */
@Configuration
public class ApplicationUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeanException {
        ApplicationUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取bean
     * @author liweimin
     * @date 2020/10/12
     * @param name name
     * @param cla class
     * @return T
     **/
    public <T> T getBean(String name, Class<T> cla){
        return applicationContext.getBean(name,cla);
    }

    /**
     * 获取bean
     * @author liweimin
     * @date 2020/10/12
     * @param name name
     * @return T
     **/
    public static <T> T getBean(String name){
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获取bean
     * @author liweimin
     * @date 2020/10/12
     * @param cla class
     * @return T
     **/
    public static <T> T getBean(Class<T> cla){
        return applicationContext.getBean(cla);
    }
}