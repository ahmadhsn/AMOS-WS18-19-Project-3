package com.jwt.service;

import org.json.JSONObject;

import com.jwt.model.Address;
import com.jwt.model.Route;

public class NewRouteRequest {
	private Address startAddress;
	private Address endAddress;
	private Route route;
	
	public NewRouteRequest(JSONObject JSONreq) {
		startAddress = new Address(JSONreq.getJSONObject("start_address"));
		endAddress = new Address(JSONreq.getJSONObject("end_address"));
		route = new Route(JSONreq.getJSONObject("route"));
	}

	public Address getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(Address startAddress) {
		this.startAddress = startAddress;
	}

	public Address getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(Address endAddress) {
		this.endAddress = endAddress;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
}
