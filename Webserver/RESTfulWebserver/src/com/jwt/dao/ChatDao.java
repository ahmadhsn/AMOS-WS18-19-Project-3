package com.jwt.dao;

import java.util.ArrayList;

import org.json.JSONArray;

import com.jwt.model.Chat;
import com.jwt.model.Message;

public interface ChatDao {

	public void saveMessage(Message msg);
	public int loadChat(JSONArray jsonParticipants);
	public ArrayList<Chat> loadAllChats(int userId);
}
