package com.jwt.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.Address;

public class AddressDaoImplementation implements AddressDao {
	public void createAddress(Address address) {
		try {
			DatabaseProvider provider = DatabaseProvider.getInstance();
			String sqlQuerry = "INSERT INTO public.address(country, state, city, street, postcode, housenumber, longitude, latitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement st = provider.queryInsertDB(sqlQuerry, address.getCountry(), address.getState(), address.getCity(),
					address.getStreet(), address.getPostcode(), address.getHousenumber(), address.getLongitude(), address.getLatitude());

	        try (ResultSet generatedKeys = st.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	address.setId(generatedKeys.getInt(1));
	            }
	            else {
	                throw new SQLException("Creating address failed, no ID obtained.");
	            }
	        }
			st.closeOnCompletion();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void updateAddress(Address address) {
		// TODO: Implement
	}
	public void removeAddress(Address address) {
		// TODO: Implement
	}

}
