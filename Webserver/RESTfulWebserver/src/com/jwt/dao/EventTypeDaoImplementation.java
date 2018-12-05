package com.jwt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.EventType;

public class EventTypeDaoImplementation implements EventTypeDao {
	public List<EventType> getEventTypes() {
		 String selectSQL = "SELECT * FROM event_type";
		 List<EventType> eventTypesList = new ArrayList<EventType>();
		 try {
			 Connection conn = DatabaseProvider.getInstance().getConnection();
			 PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
			 ResultSet rs = preparedStatement.executeQuery();
			 
			 while (rs.next()) {
				 EventType eventType = new EventType(rs.getInt("id_event_type"), rs.getString("name"), rs.getString("description"));
				 eventTypesList.add(eventType);
			 }
			 return eventTypesList;
		 }
		catch (SQLException ex){ return new ArrayList<EventType>();}
	}
}
