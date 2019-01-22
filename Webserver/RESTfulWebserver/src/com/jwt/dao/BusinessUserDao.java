package com.jwt.dao;

import com.jwt.model.BusinessUser;

public interface BusinessUserDao {

	public BusinessUser getBusinessProfile(int id);
	
	public void addBusinessProfile(BusinessUser user);
	
}
