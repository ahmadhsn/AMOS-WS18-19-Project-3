package com.jwt.dao;

import com.jwt.model.User;

public interface UserDao {
	User createUser(String email, String password, int user_type);
	User getUser(String email, String password);
}
