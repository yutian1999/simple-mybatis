/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.cache;
import com.yutian.mybatis.SqlSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author wengyz
 * @version MybatisCache.java, v 0.1 2020-11-05 11:27 上午
 */
public class MybatisCache {

    private final static Map<String,List<Object>> cache = new HashMap<>();

    public static List<Object> get(String key){
        return cache.get(key);
    }

    public static void put(String key,List<Object> value){
        cache.put(key,value);
    }

    public static String cacheKey(SqlSession session, Class aClass, String genericClassName, Object[] params, String sql){
        int sessionCode = session.hashCode();
        String className = aClass.getName();
        StringBuilder sb = new StringBuilder(className);
        Arrays.stream(params).map(Object::hashCode).forEach(r ->sb.append(r));

        String str = sb.append(sessionCode)
                .append(genericClassName)
                .append(sql).toString();

        int hashCode = str.hashCode();
        return String.valueOf(hashCode);
    }
}