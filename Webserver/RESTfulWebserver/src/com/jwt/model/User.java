package com.jwt.model;

public class User {
	private int user_id;
	private String email;
	private int user_type;
	
	public User(int user_id, String email, int user_type) {
		this.user_id = user_id;
		this.email = email;
		this.user_type = user_type;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getUser_type() {
		return user_type;
	}
	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}
}
