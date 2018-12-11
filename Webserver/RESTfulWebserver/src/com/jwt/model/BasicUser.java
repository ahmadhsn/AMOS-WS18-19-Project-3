package com.jwt.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BasicUser {
	private int userId;
	private String firstName;
	private String lastName;
	private String email;
	private boolean friendstatus;

	public BasicUser(int userId, String firstName, String lastName, String email) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.friendstatus = false;
	}
	
	public BasicUser(int userId, String firstName, String lastName, String email, boolean friendstatus) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.friendstatus = friendstatus;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public static JSONArray serializeUserList(List<BasicUser> userList) { 
		JSONArray userListJSON = new JSONArray();
		for (BasicUser user : userList) {
			JSONObject userObject = BasicUser.serializeUser(user);
			userListJSON.put(userObject);
		}
		return userListJSON;
	}
	
	public static JSONObject serializeUser(BasicUser user) {
		JSONObject userJSON = new JSONObject();
		userJSON.put("id", user.userId);
		userJSON.put("first_name", user.firstName);
		userJSON.put("last_name", user.lastName);
		userJSON.put("email", user.email);
		if(user.friendstatus) {
			userJSON.put("friendstatus", "friends");
		}
		return userJSON;
	}

}
