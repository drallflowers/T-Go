package com.may1722.t_go.model;

import java.sql.Date;

/**
 * Created by Michael Phippen on 1/30/2017.
 */

public class UserObject {

    //Account Info
    private int userid;
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
}
