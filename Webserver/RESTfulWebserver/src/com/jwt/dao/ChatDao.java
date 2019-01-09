package com.jwt.dao;

import org.json.JSONArray;

import com.jwt.model.Message;

public interface ChatDao {

	public void saveMessage(Message msg);
	public int loadChat(JSONArray jsonParticipants);
}
