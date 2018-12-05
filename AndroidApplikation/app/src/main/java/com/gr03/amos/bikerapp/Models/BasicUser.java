package com.gr03.amos.bikerapp.Models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BasicUser  {
    @PrimaryKey
    private long user_id;
    private String firstName;
    private String lastName;
    private String email;

    public BasicUser(Long user_id, String firstName, String lastName, String email){
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getUser_id() {
        return user_id;
    }


}