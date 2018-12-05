package com.jwt.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.Address;
import com.jwt.model.Event;

public class EventDaoImplementation implements EventDao {
	public void createEvent(Event event, Address address) {
		try {
			AddressDao addressDao = new AddressDaoImplementation();
			addressDao.createAddress(address);
			cteateEvent(event, address.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	void cteateEvent(Event event, int addressID) throws SQLException {
		String sqlQuerry = "INSERT INTO event(id_user, name, description, id_address, id_event_type, date, time)VALUES (?, ?, ?, ?, ?, ?, ?);";
		DatabaseProvider provider = DatabaseProvider.getInstance();
		PreparedStatement st = provider.queryInsertDB(sqlQuerry, event.getUser_id(), event.getName(), event.getDescription(), addressID, event.getEventTypeID(), Date.valueOf(event.getDate()), Time.valueOf(event.getTime()));
		  try (ResultSet generatedKeys = st.getGeneratedKeys()) {
		   if (generatedKeys.next()) {
			   event.setEvent_id(generatedKeys.getInt(1));
		   } else {
		    throw new SQLException("Creating e failed, no ID obtained.");
		   }
		  }
		  st.closeOnCompletion();
		 }
}
