package com.gr03.amos.bikerapp.Models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Chat extends RealmObject {
    @PrimaryKey
    private Long id_chat;
    private RealmList<Integer> participants;
    private String last_send;
    private String last_message;
    private String title;


    public Long getId_chat() {
        return id_chat;
    }

    public void setId_chat(Long id_chat) {
        this.id_chat = id_chat;
    }

    public RealmList<Integer> getParticipants() {
        return participants;
    }

    public void setParticipants(RealmList<Integer> participants) {
        this.participants = participants;
    }

    public String getLast_send() {
        return last_send;
    }

    public void setLast_send(String last_send) {
        this.last_send = last_send;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
