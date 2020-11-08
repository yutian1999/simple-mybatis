/**
 * aljk.com
 * Copyright (C) 2013-2020All Rights Reserved.
 */
package com.yutian.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author wengyz
 * @version Mapper.java, v 0.1 2020-11-02 9:35 上午
 */
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {
}