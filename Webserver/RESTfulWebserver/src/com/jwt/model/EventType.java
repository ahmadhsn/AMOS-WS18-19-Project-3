package com.jwt.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class EventType {
	public EventType(int id, String name, String description) {
		this.ID = id;
		this.name = name;
		this.description = description;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
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
	private int ID;
	private String name;
	private String description;
	
	public static JSONArray serializeEventTypeList(List<EventType> eventTypeList) { 
		JSONArray eventTypeListJSON = new JSONArray();
		for (EventType eventType : eventTypeList) {
			JSONObject eventTypeObject = EventType.serializeEventType(eventType);
			eventTypeListJSON.put(eventTypeObject);
		}
		return eventTypeListJSON;
	}
	
	public static JSONObject serializeEventType(EventType eventType) {
		JSONObject eventTypeJSON = new JSONObject();
		eventTypeJSON.put("id", eventType.ID);
		eventTypeJSON.put("name", eventType.name);
		eventTypeJSON.put("description", eventType.description);
		return eventTypeJSON;
	}
}
