package com.gr03.amos.bikerapp.Models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Address extends RealmObject {
    private String country;
    @PrimaryKey
    private long id_address;
    private String city;
    private String house_number;
    private String street;
    private long postcode;

    public long getId_address() {
        return id_address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public long getPostcode() {
        return postcode;
    }

    public void setPostcode(long postcode) {
        this.postcode = postcode;
    }
}
