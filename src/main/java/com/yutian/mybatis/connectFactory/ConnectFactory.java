/**
 * aljk.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.yutian.mybatis.connectFactory;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author wengyz
 * @version ConnectFactory.java, v 0.1 2020-06-05 7:31 下午
 */
public class ConnectFactory {

    private static Logger logger = LoggerFactory.getLogger(ConnectFactory.class.getName());

    private static volatile LinkedBlockingDeque<Connection> pools = new LinkedBlockingDeque();

    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USER = "jdbc.user";
    private static final String JDBC_PASSWORD = "jdbc.password";
    private static final String FILE_PATH = "mybatis.properties";

    private static String url;
    private static String user;
    private static String password;


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ClassPathResource classPathResource = new ClassPathResource(FILE_PATH);
            List<String> lines = FileUtils.readLines(classPathResource.getFile(), Charsets.UTF_8.name());
            for (String line : lines) {
                String[] content = line.split("=");
                switch (content[0]) {
                    case JDBC_URL:
                        url = content[1];
                        break;
                    case JDBC_USER:
                        user = content[1];
                        break;
                    case JDBC_PASSWORD:
                        password = content[1];
                        break;
                }
            }
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void init(){
        for (int i = 0; i < 4; i++) {
            pools.add(connect());
        }
    }

    public static void destroy() {
        for (Connection conn : pools) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        pools.clear();
    }

    public static Connection fetchConnect(){
        Connection connection = pools.poll();
        try {
            if (connection != null && !connection.isClosed()){
                return connection;
            }
        } catch (SQLException e) {
            logger.error("fetchConnect error = ",e);
        }
        init();
        return pools.poll();
    }

    public static void close(Connection connection){
        pools.add(connection);
    }

    public static synchronized Connection connect(){
        try {
            return DriverManager.getConnection(url, user, password);
        }catch (Exception e){
            logger.error("connect error = ",e);
            throw new RuntimeException(e.getMessage());
        }
    }
}