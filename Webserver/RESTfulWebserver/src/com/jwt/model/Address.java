package com.jwt.model;

import org.json.JSONObject;

public class Address {
	private int id;
	private String country;
	private String state;
	private String city;
	private String street;
	private int postcode;
	private String housenumber;
	private Double longitude;
	private Double latitude;
	public Address(String country, String state, String city, int postcode, String housenumber, Double longitude, Double latitude) {
		this.country = country;
		this.state = state;
		this.city = city;
		this.postcode = postcode;
		this.housenumber = housenumber;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Address(JSONObject addressJSON) {
		this.country = addressJSON.getString("country");
		this.city = addressJSON.getString("city");
		this.state = addressJSON.optString("state");
		this.postcode = addressJSON.optInt("postcode");
		this.housenumber = addressJSON.optString("housenumber");
		this.latitude = addressJSON.optDouble("latitude");
		this.longitude = addressJSON.optDouble("longitude");
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitde) {
		this.longitude = longitde;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getPostcode() {
		return postcode;
	}
	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}
	public String getHousenumber() {
		return housenumber;
	}
	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
}
