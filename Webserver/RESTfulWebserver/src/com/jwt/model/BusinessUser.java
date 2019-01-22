package com.jwt.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BusinessUser {

	private int userId;
	private String businessName;
	private String businessDescription;
	private String email;
	private Address businessAddress;

	public BusinessUser(int userId, String businessName, String businessDescription, String email) {
		this.userId = userId;
		this.businessName = businessName;
		this.businessDescription = businessDescription;
		this.email = email;
	}
	
	public BusinessUser(JSONObject jsonUser) {
		this.userId = jsonUser.getInt("id_user");
		this.businessName = jsonUser.getString("business_name");
		this.businessDescription = jsonUser.getString("business_descr");
		this.businessAddress = new Address(jsonUser.getJSONObject("business_address"));
	}
		
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessDescription() {
		return businessDescription;
	}

	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(Address businessAddress) {
		this.businessAddress = businessAddress;
	}

	public static JSONArray serializeBusinessUserList(List<BusinessUser> userList) { 
		JSONArray userListJSON = new JSONArray();
		for (BusinessUser user : userList) {
			JSONObject userObject = BusinessUser.serializeBusinessUser(user);
			userListJSON.put(userObject);
		}
		return userListJSON;
	}
	
	public static JSONObject serializeBusinessUser(BusinessUser user) {
		JSONObject userJSON = new JSONObject();
		userJSON.put("id", user.userId);
		userJSON.put("business_descr", user.businessDescription);
		userJSON.put("business_name", user.businessName);
		if(user.businessAddress != null) {
			userJSON.put("business_address", Address.serializeAddress(user.businessAddress));
		}
		
		return userJSON;
	}


}
