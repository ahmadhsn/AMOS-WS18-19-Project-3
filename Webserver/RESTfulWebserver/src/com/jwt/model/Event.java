package com.jwt.model;

import org.json.JSONObject;

public class Event {
	private int event_id;
	private int user_id;
	private int address_id;
	private int eventTypeID;
	private String name;
	private String description;
	private String date;
	private String time;
	
	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	
	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getEventTypeID() {
		return eventTypeID;
	}

	public void setEventTypeID(int event_type_id) {
		this.eventTypeID = event_type_id;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Event(JSONObject JSONOobj, int user_id) {
 		this.user_id = user_id;
		this.name = JSONOobj.getString("name");
		this.description = JSONOobj.getString("description");
		this.date = JSONOobj.getString("date");
		this.time = JSONOobj.getString("time");
		this.eventTypeID = JSONOobj.getInt("event_type_id"); 
	}
}
