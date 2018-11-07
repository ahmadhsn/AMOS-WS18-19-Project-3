package com.gr03.amos.bikerapp;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLExample {

    public Connection getPostgreSQLConnection() {
        /* Declare and initialize a sql Connection variable. */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection ret = null;

        try {

            /* Register jdbc driver class. */
            Class.forName("org.postgresql.Driver");
            /* Create connection url. */
            String mysqlConnUrl = "jdbc:postgresql://10.0.2.2:5432/test";

            /* user name. */
            String mysqlUserName = "postgres";

            /* password. */
            String mysqlPassword = "toor";

            /* Get the Connection object. */
            ret = DriverManager.getConnection(mysqlConnUrl, mysqlUserName, mysqlPassword);

            /* Get related meta data for this mysql server to verify db connect successfully.. */
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
