package com.jwt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.BasicUser;

public class UserDaoImplementation implements UserDao {

	DatabaseProvider db; 
	
	public UserDaoImplementation() {
		db = DatabaseProvider.getInstance();
	}
	
	@Override
	public BasicUser getUser(int id) {
		//TODO change to arguments syntax
		ResultSet rsUser = db.querySelectDB("SELECT * FROM basic_user b, user_reg u WHERE b.id_user = u.id_user AND b.id_user = " + id);

		try {
			if(rsUser.next()) {
				return new BasicUser(rsUser.getInt("id_user"), rsUser.getString("first_name"), rsUser.getString("last_name"),rsUser.getString("email") );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<BasicUser> getAllUser() {
		ResultSet rsUser = db.querySelectDB("SELECT * FROM basic_user b, user_reg u WHERE b.id_user = u.id_user");
		return getListByRS(rsUser);
	}

	@Override
	public List<BasicUser> getFriends(int id) {
		//TODO change to arguments syntax
		ResultSet rsFriends = db.querySelectDB("SELECT b.id_user, b.first_name, b.last_name, b.dob, u.email FROM basic_user b, friendship f, user_reg u WHERE u.id_user = b.id_user AND b.id_user = f.id_user2 AND f.id_user1 = "+ id);
		
		return getListByRS(rsFriends);
	}
	
	private List<BasicUser> getListByRS(ResultSet rs){
		List<BasicUser> user = new ArrayList<>();
		try {
			while(rs.next()) {
				user.add(new BasicUser(rs.getInt("id_user"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email") ));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
	
	

}
