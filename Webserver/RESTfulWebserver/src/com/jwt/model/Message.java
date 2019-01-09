package com.jwt.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Message {

	private int id;
	private int idChat;
	private int idUser;
	private Timestamp time;
	private String msg;
	
	public Message(int idChat, int idUser, Timestamp time, String msg) {
		this.idChat = idChat;
		this.idUser = idUser;
		this.time = time; 
		this.msg = msg;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdChat() {
		return idChat;
	}
	
	public void setIdChat(int idChat) {
		this.idChat = idChat;
	}
	
	public int getIdUser() {
		return idUser;
	}
	
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
