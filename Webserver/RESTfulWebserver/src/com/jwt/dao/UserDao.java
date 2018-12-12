package com.jwt.dao;

import java.util.List;
import com.jwt.model.BasicUser;

public interface UserDao {
	BasicUser getUser(int id);

	List<BasicUser> getAllUser();

	List<BasicUser> getFriends(int id);
	
	boolean getFriendById(int userId, int friendId);
	
	List<BasicUser> searchUser(String input, int userId);
	
	BasicUser getAdditionalInfo(int userId);

}
