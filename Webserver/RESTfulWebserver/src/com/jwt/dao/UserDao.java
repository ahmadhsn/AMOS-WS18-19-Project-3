package com.jwt.dao;

import java.util.List;

import com.jwt.model.BasicUser;
import com.jwt.model.User;


public interface UserDao {
    List<BasicUser> getAllUser();
    List<BasicUser> getFriends(int id);
    List<BasicUser> searchUser(String input, int userId);
    
    BasicUser getAdditionalInfo(int userId);
    BasicUser getUser(int id);
    
    User createUser(String email, String password, int user_type);
    User getUser(String email, String password);
    
    String changeUserPassword(String email);
    
    boolean getFriendById(int userId, int friendId);
}
