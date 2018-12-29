package com.gr03.amos.bikerapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Route extends RealmObject {
    @PrimaryKey
    private long id_route;
    private long id_user;
    private String name;
    private String description;
    private Address start_address;
    private Address end_address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getStartAddress() {
        return start_address;
    }

    public void setStartAddress(Address start_address) {
        this.start_address = start_address;
    }

    public Address getEndAddress() {
        return end_address;
    }

    public void setEndAddress(Address end_address) {
        this.end_address = end_address;
    }

    public long getId_route() {
        return id_route;
    }

    public void setId_route(long id_route) {
        this.id_route = id_route;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }
}


