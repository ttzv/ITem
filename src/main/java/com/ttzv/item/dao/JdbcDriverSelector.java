package com.ttzv.item.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcDriverSelector { //todo: refactor to implement DataSource and decouple configuration
    
    private final static String POSTGRES = "POSTGRES";
    private final static String MYSQL = "MYSQL";
    
    private String jdbcDriver;
    private Properties properties;
    private String dbUrlPrefix;
    private String address;
    private String user;
    private char[] password;
    
    
    public JdbcDriverSelector(String jdbcDriver, String address, String user, char[] password) {
        this.jdbcDriver = jdbcDriver;
        this.address = address;
        this.user = user;
        this.password = password;
    }
    
    private void buildDriverProperties(){
        switch (jdbcDriver) {
            case POSTGRES:
                dbUrlPrefix = "jdbc:postgresql://";
                properties = new Properties();
                properties.setProperty("user", user);
                properties.setProperty("password", new String(password));
                break;
            case MYSQL: //todo:  implement
                
                break;
            default:
                System.err.println("Undefined driver name");
                break;
        }
    }
    
    public Connection createConnection() throws SQLException {
        buildDriverProperties();
        String dbUrl = dbUrlPrefix + address;
        return DriverManager.getConnection(dbUrl, properties);
    }
}
