package com.jwt.DataBaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Date;

import javax.servlet.ServletContext;

public class DatabaseProvider {
    private static DatabaseProvider instance;
    private Connection connection;
    Config config = Config.getInstance();
    private String postgresURL = "jdbc:postgresql://" + config.host + ":5432/" + config.database;
    
    private DatabaseProvider() throws SQLException {
        try {
        	Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(postgresURL, config.username, config.password);
        } catch (SQLException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        } catch(ClassNotFoundException ex) {
        	System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public static synchronized DatabaseProvider getInstance(ServletContext context) throws SQLException {
        if (instance == null) {
        	Config conf = Config.getInstance();
        	conf.setContext(context);
            instance = new DatabaseProvider();
        } else if (instance.getConnection().isClosed()) {
        	Config conf = Config.getInstance();
        	conf.setContext(context);
            instance = new DatabaseProvider();
        }
        return instance;
    }
    
    public static synchronized DatabaseProvider getInstance() {
        if (instance == null) {
        	System.out.println("Could not find the context for the DatabaseProvider instance, make sure you called DatabaseProvider.getInstance(context) in the upstream code");
        }
        return instance;
    }
    
	public PreparedStatement queryInsertDB(String query, Object... arguments) {
		System.out.println("...... Execute Insert Query: " + query);
		try {
			if (this.connection.isClosed()) {
				this.connection = DriverManager.getConnection(postgresURL, config.username, config.password);
			}
	
			PreparedStatement preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			for (int i = 0; i < arguments.length; i++) {
				if (arguments[i] instanceof String) {
					String string = (String) arguments[i];
					preparedStatement.setString(i + 1, string);
				} else if (arguments[i] instanceof Integer) {
					int integer = (Integer)arguments[i];
					preparedStatement.setInt(i + 1, integer);
				} else if (arguments[i] instanceof Date) {
					Date date = (Date)arguments[i];
					preparedStatement.setDate(i+1, date);
				} else if (arguments[i] instanceof Time) {
					Time time = (Time)arguments[i];
					preparedStatement.setTime(i+1, time);
				} else {
					//TODO: improve this with additional instances of 
					preparedStatement.setString(i + 1, (String)arguments[i]);
				}
			}
			if (preparedStatement.executeUpdate() < 0) {
				throw new SQLException("Creating Route failed, no rows affected.");
			}
			preparedStatement.closeOnCompletion();
			return preparedStatement;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (this.connection != null) {
				try {
					this.connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("...... Insert into database done");
		return null;
	}

	public ResultSet querySelectDB(String query, String... arguments) {
		ResultSet rs = null;
		System.out.println("...... Execute Select Query: " + query);

		try {
			if (this.connection.isClosed()) {
				this.connection = DriverManager.getConnection(postgresURL, config.username, config.password);
			}
			PreparedStatement ur = this.connection.prepareStatement(query);

			for (int i = 0; i < arguments.length; i++) {
				ur.setString(i + 1, arguments[i]);
			}
			rs = ur.executeQuery();
			ur.closeOnCompletion();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (this.connection != null) {
				try {
					this.connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("...... Select Statement Successful");
		return rs;
	}
	
	public ResultSet querySelectDB(String query) {
		ResultSet rs = null;
		System.out.println("...... Execute Select Query: " + query);
		try {
			if (this.connection.isClosed()) {
				this.connection = DriverManager.getConnection(postgresURL, config.username, config.password);
			}
			Statement ss = this.connection.createStatement();
			rs = ss.executeQuery(query);
			ss.closeOnCompletion();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (this.connection != null) {
				try {
					this.connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("...... Select Statement Successful");

		return rs;
	}
}