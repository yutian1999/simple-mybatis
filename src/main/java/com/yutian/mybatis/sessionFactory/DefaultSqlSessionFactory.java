/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.sessionFactory;

import com.yutian.mybatis.SqlSession;
import com.yutian.mybatis.SqlSessionFactory;
import com.yutian.mybatis.session.DefaultSqlSession;

/**
 *
 * @author wengyz
 * @version DefaultSqlSessionFactory.java, v 0.1 2020-11-05 2:31 下午
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession();
    }
}