package com.jwt.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.Message;

public class ChatDaoImplementation implements ChatDao {

    DatabaseProvider db;

    public ChatDaoImplementation() {
        db = DatabaseProvider.getInstance();
    }

	@Override
	public void saveMessage(Message msg) {
		PreparedStatement stmt = db.queryInsertDB("INSERT INTO message (id_chat, id_user, time_created, message) VALUES (?,?,?,?)", msg.getIdChat(), msg.getIdUser(), msg.getTime(), msg.getMsg());
		try {
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				msg.setId(rs.getInt(0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public int createChat(int... idParticipants) {
		//insert chat into db 
		PreparedStatement stmt = db.queryInsertDB("INSERT INTO chat VALUES NULL");
		int idChat = -1; 
		try {
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				idChat = rs.getInt(0);
				
				//insert participants of chat into db
				for(int participant: idParticipants) {
					db.queryInsertDB("INSERT INTO participants VALUES (?, ?)", participant, idChat);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idChat;
		
	}
    

}
