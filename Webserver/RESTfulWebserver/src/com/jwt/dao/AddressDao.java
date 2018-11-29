package com.jwt.dao;

import com.jwt.model.Address;

public interface AddressDao {
	void createAddress(Address address);
	void updateAddress(Address address);
	void removeAddress(Address address);
}
