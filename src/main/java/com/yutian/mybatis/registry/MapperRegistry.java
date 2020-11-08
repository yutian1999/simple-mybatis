/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.registry;

import com.yutian.mybatis.annotation.Select;
import com.yutian.mybatis.bind.SqlBind;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wengyz
 * @version MapperRegistry.java, v 0.1 2020-11-02 10:46 上午
 */
public class MapperRegistry {

    private static final Map<String, SqlBind> knownMappers = new HashMap<>();

    public static <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            Method[] methods = type.getDeclaredMethods();
            for (Method method : methods) {
                String key = getKey(type.getName(), method.getName());
                Select annotation = method.getAnnotation(Select.class);
                try {
                    Type genericReturnType = method.getGenericReturnType();
                    String typeName = genericReturnType.getTypeName();
                    String className = null;
                    if (typeName.contains("<") && typeName.contains(">")){
                        className = typeName.substring(typeName.indexOf("<") + 1,typeName.indexOf(">"));
                    }
                    knownMappers.put(key,new SqlBind(annotation.sql().toLowerCase(),method.getParameterTypes(),method.getReturnType(),className));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T> String getKey(String className, String methodName) {
        return className + "#" + methodName;
    }


    public static <T> SqlBind<T> getMapper(Method method){
        return knownMappers.get(getKey(method.getDeclaringClass().getName(), method.getName()));
    }
}