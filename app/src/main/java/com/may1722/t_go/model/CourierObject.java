package com.may1722.t_go.model;

import java.util.ArrayList;

public class CourierObject {

    private int user_id;
    //TODO Change to job object when added
    private ArrayList<Object> currentJobs;

    public void addJob(Object o)
    {
        currentJobs.add(o);
    }

    public void removeJob(Object o)
    {
        currentJobs.remove(o);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
