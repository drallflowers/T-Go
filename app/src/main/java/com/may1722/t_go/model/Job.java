package com.may1722.t_go.model;

/**
 * Created by David on 1/29/2017.
 */

public class Job {

    UserObject owner;
    private String description;
    private String title;
    private String time;
    private String address;
    private Double price;


    //need to  uncomment the User field after merge
    public Job(UserObject u, String desc, String ttl, Double p, String t, String adr){
        this.price = p;
        this.description = desc;
        this.title = ttl;
        this.time = t;
        this.address = adr;
        this.owner = u;
    }


    public String getDescription(){
        return this.description;
    }

    public String getAddress(){
        return this.address;
    }

    public String getTitle(){
        return this.title;
    }

    public String getTime(){
        return this.time;
    }

    public Double getPrice(){
        return this.price;
    }
}
