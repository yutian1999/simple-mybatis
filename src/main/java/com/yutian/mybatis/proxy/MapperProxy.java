/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.proxy;
import com.yutian.mybatis.SqlSession;
import com.yutian.mybatis.bind.SqlBind;
import com.yutian.mybatis.registry.MapperRegistry;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author wengyz
 * @version MapperProxy.java, v 0.1 2020-11-02 10:59 上午
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private Class<T> aClass;

    private SqlSession session;

    public MapperProxy(Class<T> aClass, SqlSession session) {
        this.aClass = aClass;
        this.session = session;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        SqlBind<T> sqlBind = MapperRegistry.getMapper(method);
        List<Object> execute = session.select(sqlBind.getReturnType(), sqlBind.getGenericClassName(), args, sqlBind.getSql());
        if(sqlBind.getReturnType().getName().equals("java.util.ArrayList")){
            return execute;
        }

        if (execute.size() > 0 && sqlBind.getGenericClassName() == null){
            return execute.get(0);
        }

        return null;
    }
}