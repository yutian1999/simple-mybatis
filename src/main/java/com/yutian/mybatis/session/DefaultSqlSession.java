/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.session;

import com.yutian.mybatis.Executor;
import com.yutian.mybatis.SqlSession;
import com.yutian.mybatis.cache.MybatisCache;
import com.yutian.mybatis.executor.BaseExecutor;

import java.util.List;

/**
 *
 * @author wengyz
 * @version DefaultSqlSession.java, v 0.1 2020-11-05 11:23 上午
 */
public class DefaultSqlSession implements SqlSession {

    @Override
    public <T> List<Object> select(Class<T> aClass, String genericClassName, Object[] params, String sql) {
        String cacheKey = MybatisCache.cacheKey(this,aClass, genericClassName, params, sql);
        List<Object> objects = MybatisCache.get(cacheKey);
        if (objects != null){
            return objects;
        }
        Executor executor = new BaseExecutor(sql);
        objects = executor.execute(aClass, genericClassName, params);
        MybatisCache.put(cacheKey,objects);
        return objects;
    }
}