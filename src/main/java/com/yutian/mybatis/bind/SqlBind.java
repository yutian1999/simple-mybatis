/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.bind;

import java.io.Serializable;

/**
 *
 * @author wengyz
 * @version SqlBind.java, v 0.1 2020-11-03 10:34 上午
 */
public class SqlBind<T> implements Serializable {

    private String sql;

    private Class<T>[] paramsType;

    private Class<T> returnType;

    private String genericClassName;

    public SqlBind(String sql, Class<T>[] paramsType, Class<T> returnType, String genericClassName) {
        this.sql = sql;
        this.paramsType = paramsType;
        this.returnType = returnType;
        this.genericClassName = genericClassName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class<T>[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(Class<T>[] paramsType) {
        this.paramsType = paramsType;
    }

    public Class<T> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<T> returnType) {
        this.returnType = returnType;
    }

    public String getGenericClassName() {
        return genericClassName;
    }

    public void setGenericClassName(String genericClassName) {
        this.genericClassName = genericClassName;
    }
}