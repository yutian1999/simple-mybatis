/**
 * aljk.com
 * Copyright (C) 2013-2020All Rights Reserved.
 */
package com.yutian.mybatis;

import java.util.List;

/**
 *
 * @author wengyz
 * @version Executor.java, v 0.1 2020-11-02 10:08 上午
 */
public interface Executor {

   <T> List<Object> execute(Class<T> aClass,String genericClassName, Object[] params);
}