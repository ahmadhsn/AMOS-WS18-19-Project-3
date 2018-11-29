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
			PreparedStatement st = provider.getConnection().prepareStatement(sqlQuerry, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, address.getCountry());
			st.setString(2, address.getState());
			st.setString(3, address.getCity());
			st.setString(4, address.getStreet());
			st.setInt(5, address.getPostcode());
			st.setString(6, address.getHousenumber());
			st.setDouble(7, address.getLongitude());
			st.setDouble(8, address.getLatitude());
			int affectedRows = st.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating address failed, no rows affected.");
	        }

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
