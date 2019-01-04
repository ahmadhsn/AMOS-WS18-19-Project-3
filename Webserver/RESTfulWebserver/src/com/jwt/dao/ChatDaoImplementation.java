package com.jwt.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;

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
	public int loadChat(JSONArray jsonParticipants) {
		
		ArrayList<Integer> idParticipants = new ArrayList<>();
		for(int i=0; i<jsonParticipants.length(); i++) {
			idParticipants.add(jsonParticipants.getInt(i));
		}
		
		//check if chat exists		
		StringBuilder sqlSelect = new StringBuilder();
		sqlSelect.append("SELECT * FROM chat WHERE chatId IN ");
		for(int i=0; i< idParticipants.size(); i++) {
			int currId = idParticipants.get(i);
			if(i > 0) {
				sqlSelect.append(" AND chatId IN ");
			}
			sqlSelect.append("(SELECT * FROM participants WHERE id_user = " + currId + ")");	
		}
		
		ResultSet rsChat = db.querySelectDB(sqlSelect.toString());
		
		try {
			//check if chat exists
			if(rsChat.next()) {
				return rsChat.getInt(0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//create a new chat
		return createChat(idParticipants);
				
	}
	
	private int createChat(ArrayList<Integer> participants) {
		
		//insert chat into db 
		PreparedStatement stmt = db.queryInsertDB("INSERT INTO chat VALUES NULL");
		int idChat = -1; 
		try {
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				idChat = rs.getInt(0);
				
				//insert participants of chat into db
				for(int participant: participants) {
					db.queryInsertDB("INSERT INTO participants VALUES (?, ?)", participant, idChat);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idChat;
		
		
	}
    

}
