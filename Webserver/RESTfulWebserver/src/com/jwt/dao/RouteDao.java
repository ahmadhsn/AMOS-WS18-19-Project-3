package com.jwt.dao;

import com.jwt.model.Address;
import com.jwt.model.Route;

public interface RouteDao {
	void createRoute(Address startAddress, Address endAddress, Route route);
	void updateRoute(Route route);
	void removeRoute(Route route);
}
