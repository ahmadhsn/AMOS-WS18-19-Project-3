package com.jwt.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jwt.DataBaseConnection.DatabaseProvider;

public class Chat {
	int idChat;
	ArrayList<Integer> participants;
	Timestamp lastSend;
	String lastMessage; 
	String title;
	
	public Chat(int idChat, ArrayList<Integer> participants, Timestamp lastSend, String lastMessage, String title) {
		this.idChat = idChat;
		this.participants = participants;
		this.lastSend = lastSend;
		this.lastMessage = lastMessage; 
		this.title = title;
	}
	
	public static JSONArray serializeChatList(List<Chat> chatList) { 
		JSONArray chatListJSON = new JSONArray();
		for (Chat chat : chatList) {
			JSONObject chatObject = Chat.serializeChat(chat);
			chatListJSON.put(chatObject);
		}
		return chatListJSON;
	}
	
	public static JSONObject serializeChat(Chat chat) {
		JSONObject chatJSON = new JSONObject();
		chatJSON.put("id_chat", chat.idChat);
		chatJSON.put("participants", chat.participants);
		chatJSON.put("last_send", chat.lastSend);
		chatJSON.put("last_message", chat.lastMessage);
		chatJSON.put("title", chat.title);
		return chatJSON;
	}
}
