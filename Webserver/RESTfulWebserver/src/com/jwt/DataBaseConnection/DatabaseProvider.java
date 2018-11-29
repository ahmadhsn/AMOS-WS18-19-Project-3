package com.jwt.DataBaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseProvider {
    private static DatabaseProvider instance;
    private Connection connection;
    Config config = Config.getInstance();
    private String postgresURL = "jdbc:postgresql://" + config.host + ":5432/" + config.database;
    
    private DatabaseProvider() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(postgresURL, config.username, config.password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }
    
    public Connection getConnection() {
        return connection;
    }

    public static synchronized DatabaseProvider getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseProvider();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseProvider();
        }
        return instance;
    }
    
	public void queryInsertDB(String query, String... arguments) {
		System.out.println("...... Execute Insert Query: " + query);
		try {
			if (this.connection.isClosed()) {
				this.connection = DriverManager.getConnection(postgresURL, config.username, config.password);
			}
	
			PreparedStatement ur = this.connection.prepareStatement(query);

			for (int i = 0; i < arguments.length; i++) {
				ur.setString(i + 1, arguments[i]);
			}
			if (ur.executeUpdate() < 0) {
				System.err.println("Executing query failed: " + query);
			}
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
		
		System.out.println("...... Insert into database done");
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