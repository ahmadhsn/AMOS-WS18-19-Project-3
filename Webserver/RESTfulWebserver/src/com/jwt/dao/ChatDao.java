package com.jwt.dao;

import com.jwt.model.Message;

public interface ChatDao {

	public void saveMessage(Message msg);
	public int createChat(int... idParticipants);
}
