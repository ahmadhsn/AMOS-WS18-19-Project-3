package com.gr03.amos.bikerapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Friend extends RealmObject {
    private String last_name;
    @PrimaryKey
    private Long id;
    private String first_name;
    private String email;
    private Route route;
    private String last_message_time;

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_message_time() {
        return last_message_time;
    }

    public void setLast_message_time(String last_message_time) {
        this.last_message_time = last_message_time;
    }

    public Long getId() {
        return id;
    }

    public static Friend getUserAsFriend(BasicUser user){
        Friend newFriend = new Friend();
        newFriend.last_name = user.getLastName();
        newFriend.first_name = user.getFirstName();
        newFriend.email = user.getEmail();
        newFriend.id = user.getUser_id();

        return newFriend;
    }

    public Route getRoute() {
        return route;
    }
}
