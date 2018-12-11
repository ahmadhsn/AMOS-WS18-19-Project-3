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
	
	public boolean getFriendById(int userId, int friendId) {
		ResultSet rsFriend = db.querySelectDB("SELECT * FROM friendship WHERE id_user1 = ? AND id_user2 = ?", userId, friendId);
		
		try {
			if(rsFriend.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public List<BasicUser> searchUser(String input, int userId){
		
		//TODO add more advanced search methods to handle input
		ResultSet rs = db.querySelectDB("SELECT u.id_user, b.first_name, b.last_name, u.email FROM user_reg u, basic_user b WHERE b.id_user = u.id_user AND u.id_user != ? AND (email = ? OR first_name = ? OR last_name = ?)", userId, input, input, input);
	
		List<BasicUser> user = new ArrayList<>();
		try {
			while(rs.next()) {
				if(getFriendById(userId, rs.getInt("id_user"))) {
					//add user with friendship status "friends"
					user.add(new BasicUser(rs.getInt("id_user"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), true ));					
				}else {
					//add user without friendship status
					user.add(new BasicUser(rs.getInt("id_user"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email") ));					
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
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
