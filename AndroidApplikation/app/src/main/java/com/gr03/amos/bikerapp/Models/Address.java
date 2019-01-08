package com.gr03.amos.bikerapp.Models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Address extends RealmObject {
    @PrimaryKey
    private long id_address;
    private String city;
    private String country;

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

}
