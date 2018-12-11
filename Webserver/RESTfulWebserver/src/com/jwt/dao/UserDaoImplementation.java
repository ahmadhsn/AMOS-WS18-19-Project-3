package com.jwt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.User;

public class UserDaoImplementation implements UserDao {
	public User createUser(String email, String password, int user_type) {
		//		TODO: implement me
		User user = new User();
		return user;
	}
	
	public User getUser(String email, String password) {
		User user = null;
		String selectSQL = "SELECT * FROM user_reg WHERE email=\'"+email+"\' AND password=\'"+password+"\'";
		 try {
			 DatabaseProvider conn = DatabaseProvider.getInstance();
			 ResultSet result = conn.querySelectDB(selectSQL);
			 
			 while (result.next()) {
				 user = new User(result.getInt("id_user"), result.getString("email"), result.getInt("id_user_type"));
			 }
			 return user;
		 }
		catch (SQLException ex) { return null;}
	}
}
