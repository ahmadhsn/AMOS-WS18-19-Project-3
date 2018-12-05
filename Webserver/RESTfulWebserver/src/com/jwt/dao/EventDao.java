package com.jwt.dao;

import com.jwt.model.Address;
import com.jwt.model.Event;

public interface EventDao {
	void createEvent(Event event, Address address);
}
