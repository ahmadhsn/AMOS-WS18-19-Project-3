package com.jwt.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.Address;
import com.jwt.model.BusinessUser;

public class BusinessUserDaoImplementation implements BusinessUserDao {

	DatabaseProvider db;
	
	public BusinessUserDaoImplementation() {
		db = DatabaseProvider.getInstance();
	}
	@Override
	public BusinessUser getBusinessProfile(int id) {
		String query = "SELECT u.id_user, u.email, b.name, b.description, a.country, a.state, a.city, a.postcode, a.street, a.housenumber, a.longitude, a.latitude "
				+ "FROM user_reg u, business_customer b, address a "
				+ "WHERE u.id_user = b.id_user AND b.id_address = a.id_address "
				+ "AND u.id_user = ?";	
		ResultSet rsProfile = db.querySelectDB(query, id);
		
		try {
			if(rsProfile.next()) {
				BusinessUser res = new BusinessUser(rsProfile.getInt("id_user"), rsProfile.getString("name"), rsProfile.getString("description"), rsProfile.getString("email"));
				Address bAddress = new Address(rsProfile.getString("country"), rsProfile.getString("state"),
						rsProfile.getString("city"), rsProfile.getInt("postcode"), rsProfile.getString("street"), rsProfile.getString("housenumber"), 
						rsProfile.getDouble("latitude"), rsProfile.getDouble("longitude"));
				res.setBusinessAddress(bAddress);
				
				return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	@Override
	public void addBusinessProfile(BusinessUser user) {
		AddressDao addressD = new AddressDaoImplementation();
		
		//profile exists, update profile
		if(businessProfileExists(user)) {
			addressD.createAddress(user.getBusinessAddress());
			db.queryInsertDB("UPDATE business_customer SET name = ?, id_address = ?, description = ? WHERE id_user = ?", user.getBusinessName(), user.getBusinessAddress().getId(), user.getBusinessDescription(), user.getUserId());
			
		}else {
			addressD.createAddress(user.getBusinessAddress());
			//insert new profile
			db.queryInsertDB("INSERT INTO business_customer VALUES (?,?,?,?)", 
					user.getUserId(), user.getBusinessName(), user.getBusinessAddress().getId(), 
					user.getBusinessDescription());

		}
		
	}
	
	private boolean businessProfileExists(BusinessUser user) {
		ResultSet rsBProfile = db.querySelectDB("SELECT * FROM business_customer WHERE id_user = ?", user.getUserId());
		
		try {
			if(rsBProfile.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}

}
