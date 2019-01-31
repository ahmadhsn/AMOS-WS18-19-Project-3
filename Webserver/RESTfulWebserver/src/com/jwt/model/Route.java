package com.jwt.model;

import org.json.JSONObject;

public class Route {
	private int route_id;
	private int user_id;
	private String name;
	private String description;
	private int startPoint;
	private int endPoint;
	public Route(int user_id, String name, String description, int startPoint, int endPoint) {
		this.user_id = user_id;
		this.name = name;
		this.description = description;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	public Route(JSONObject addressJSON) {
		this.user_id = addressJSON.getInt("id_user");
		this.name = addressJSON.getString("name");
		this.description = addressJSON.getString("description");
	}
	public int getId() {
		return route_id;
	}

	public void setId(int route_id) {
		this.route_id = route_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}
	public int getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(int endPoint) {
		this.endPoint = endPoint;
	}
}
