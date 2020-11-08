/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.init;
import com.yutian.mybatis.annotation.Mapper;
import com.yutian.mybatis.connectFactory.ConnectFactory;
import com.yutian.mybatis.proxy.MapperProxy;
import com.yutian.mybatis.registry.MapperRegistry;
import com.yutian.mybatis.session.DefaultSqlSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 *
 * @author wengyz
 * @version MybatisInit.java, v 0.1 2020-11-02 3:20 下午
 */
@Component
public class MybatisInit implements ApplicationContextAware{

    @Value("${mybatis.mapperPackage}")
    private String mapperPackage;

    @PostConstruct
    public void init() throws BeansException {
        String packagePath = getPackagePath(mapperPackage);
        String path = getClass().getClassLoader().getResource(packagePath).getPath();
        File file = new File(path);
        File[] files = file.listFiles();
        ClassLoader classLoader = this.getClass().getClassLoader();
        for (File child : files) {
            try {
                String filePath = child.getAbsolutePath();
                String externalName = filePath.substring(filePath.lastIndexOf('/') + 1,filePath.length() - 6);
                externalName = mapperPackage + "." + externalName;
                Class<?> aClass = classLoader.loadClass(externalName);
                MapperRegistry.addMapper(aClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    private void destroy(){
        ConnectFactory.destroy();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(Controller.class);
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Mapper resource = declaredField.getDeclaredAnnotation(Mapper.class);
                if (resource != null){
                    try {
                        declaredField.setAccessible(true);
                        declaredField.set(bean,createService(declaredField.getType()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static <T, P> T createService(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new MapperProxy<T>(interfaceClass,new DefaultSqlSession()));
    }


    private String getPackagePath(String packageName) {
        return packageName == null ? null : packageName.replace('.', '/');
    }

}