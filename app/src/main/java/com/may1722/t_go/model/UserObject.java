package com.may1722.t_go.model;

import java.util.Date;

/**
 * Created by Michael Phippen on 1/30/2017.
 */

public class UserObject {

    //Account Info
    private int user_id;
    private int user_type;
    private Date date_created;
    private int default_delivery_address_id;

    //Personal Info
    private String first_name;
    private String last_name;

    private String email;
    private String password;

    private String phone_number;
    private Date date_of_birth;

    public UserObject(int user_id, int user_type, String email, String password, String phone_number) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;

        this.date_created = new Date();
    }

    //Getters & Setters
    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public int getDefault_delivery_address_id() {
        return default_delivery_address_id;
    }

    public void setDefault_delivery_address_id(int default_delivery_address_id) {
        this.default_delivery_address_id = default_delivery_address_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
}
