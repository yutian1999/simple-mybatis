/**
 * aljk.com
 * Copyright (C) 2013-2020All Rights Reserved.
 */
package com.yutian.mybatis;

import java.util.List;

/**
 *
 * @author wengyz
 * @version SqlSession.java, v 0.1 2020-11-05 11:13 上午
 */
public interface SqlSession {

  <T>  List<Object> select(Class<T> aClass, String genericClassName, Object[] params,String sql);
}