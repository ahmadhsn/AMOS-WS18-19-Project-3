package com.jwt.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletContext;

public class DatabaseProvider {

	private String connUrl;
	private String userName;
	private String pwd;

	public DatabaseProvider(ServletContext context) {
		String realPath = context.getRealPath("/WEB-INF/adrenaline.properties");
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(new File(realPath)));

			this.connUrl = props.getProperty("dbUrl");
			this.userName = props.getProperty("dbUser");
			this.pwd = props.getProperty("dbPwd");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public Connection getPostgreSQLConnection() {
		Connection ret = null;

		try {

			Class.forName("org.postgresql.Driver");

			ret = DriverManager.getConnection(connUrl, userName, pwd);

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

	public ResultSet querySelectDB(String query) {
		ResultSet rs = null;
		Connection conn = null;
		
		try {
	        conn = this.getPostgreSQLConnection();
	        
	        
	        Statement ss= conn.createStatement();
		    rs = ss.executeQuery(query);		
		    ss.closeOnCompletion();

		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rs;

	}
	
	public void queryInsertDB(String query, String... arguments) {
		Connection conn = this.getPostgreSQLConnection();
		
        try {
            PreparedStatement ur = conn.prepareStatement (query);
            
            for(int i=0; i<arguments.length; i++) {
            	ur.setString(i+1, arguments[i]);
            }
            if(ur.executeUpdate() < 0) {
            	System.err.println("Executing query failed: " + query);
            }
            ur.closeOnCompletion();
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
