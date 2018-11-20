package com.jwt.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLExample {

    public Connection getPostgreSQLConnection() {
        Connection ret = null;

        try {

            Class.forName("org.postgresql.Driver");
            String mysqlConnUrl = "jdbc:postgresql://localhost:5432/AdrenalineDatabase";
            String mysqlUserName = "postgres";
            String mysqlPassword = "abc123";

            ret = DriverManager.getConnection(mysqlConnUrl, mysqlUserName, mysqlPassword);

            DatabaseMetaData dbmd = ret.getMetaData();
            String dbName = dbmd.getDatabaseProductName();
            String dbVersion = dbmd.getDatabaseProductVersion();
            String dbUrl = dbmd.getURL();
            
            String userName = dbmd.getUserName();
            String driverName = dbmd.getDriverName();

            System.out.println("Database Name is " + dbName);
            System.out.println("Database Version is " + dbVersion);
            System.out.println("Database Connection Url is " + dbUrl);
            System.out.println("Database User Name is " + userName);
            System.out.println("Database Driver Name is " + driverName);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
