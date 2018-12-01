package com.gr03.amos.bikerapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProfileBasic extends RealmObject {

    @PrimaryKey
    private long id_user;
    private long id_gender;
    private long id_address;
    private String first_name;
    private String last_name;
    private String date_of_birth;
    private String user_gender;
    private String user_street;
    private String hnumber;
    private String user_postcode;
    private String user_city;
    private String user_country;

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String fname) {
        this.first_name = fname;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lname) {
        this.last_name = lname;
    }

    public String getDateOfBirth() {
        return date_of_birth;
    }

    public void setDateOfBirth(String dob) {
        this.date_of_birth = dob;
    }

    public String getGender() {
        return user_gender;
    }

    public void setGender(String usergender) {
        this.user_gender = usergender;
    }

    public String getStreet() {
        return user_street;
    }

    public void setStreet(String userstreet) {
        this.user_street = userstreet;
    }

    public String getNumber() {
        return hnumber;
    }

    public void setNumber(String usernumber) {
        this.hnumber = usernumber;
    }

    public String getPostcode() {
        return user_postcode;
    }

    public void setPostcode(String userpostcode) {
        this.user_postcode = userpostcode;
    }

    public String getCity() {
        return user_city;
    }

    public void setCity(String usercity) {
        this.user_city = usercity;
    }

    public String getCountry() {
        return user_country;
    }

    public void setCountry(String usercountry) {
        this.user_country = usercountry;
    }

    public long getId_user() {
        return id_user;
    }

    public long getId_gender() {
        return id_gender;
    }

    public long getId_address() {
        return id_address;
    }

}
