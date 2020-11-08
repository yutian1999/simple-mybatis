/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.executor;

import com.alibaba.fastjson.JSON;
import com.yutian.mybatis.Executor;
import com.yutian.mybatis.connectFactory.ConnectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wengyz
 * @version SqlRunner.java, v 0.1 2020-11-02 10:07 上午
 */
public class BaseExecutor implements Executor {

    private Logger logger = LoggerFactory.getLogger(BaseExecutor.class.getName());

    private String sql;

    public BaseExecutor(String sql) {
        this.sql = sql;
    }

    public <T> List<Object> execute(Class<T> aClass,String genericClassName, Object[] params) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            List<Object> list = new ArrayList<Object>();
            connection = ConnectFactory.fetchConnect();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1,params[i]);
            }
            logger.info("executor sql = {}",sql);
            ResultSet res = preparedStatement.executeQuery();
            int offset = 0;
            while (res.next()) {
                if(offset > 0 && genericClassName == null){
                    throw new RuntimeException("too many result,but expect one");
                }
               dealResult(aClass,genericClassName,list,res);
               offset ++;
            }
            logger.info("executor sql result = {}", JSON.toJSONString(list));
            return list;
        } catch (Exception e) {
            logger.error("execute sql failure error = ",e);
            throw new RuntimeException("execute sql failure");
        } finally {
            close(preparedStatement, connection);
        }
    }

    private void close(PreparedStatement preparedStatement, Connection connection) {
        close(preparedStatement);
        ConnectFactory.close(connection);
    }

    private void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("close statement failure error = ",e);
            }
        }
    }

    private void dealResult(Class aClass,String genericClassName, List<Object> list, ResultSet res) throws InstantiationException, IllegalAccessException, SQLException {

        if (genericClassName == null){
            dealResult(aClass, list, res);
        }else {
            try {
                dealResult(Class.forName(genericClassName), list, res);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void dealResult(Class aClass, List<Object> list, ResultSet res) throws InstantiationException, IllegalAccessException, SQLException {
        Object result = aClass.newInstance();
        if (aClass.getName().equals("java.lang.String")){
            String[] cols = dealSql(sql);
            result = res.getString(cols[0].replace(" ",""));
            list.add(result);
            return;
        }

        Field[] declaredFields = result.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type.getName().contains("String")){
                field.set(result,res.getString(field.getName()));
            }else if(type.getName().contains("int")){
                field.setInt(result,res.getInt(field.getName()));
            }else if(type.getName().contains("long")){
                field.setLong(result,res.getLong(field.getName()));
            }
        }
        list.add(result);
    }

    private String[] dealSql(String sql) {
        return sql.split("from")[0].split("select")[1].split(",");
    }

}