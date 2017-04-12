package com.may1722.t_go.model;

import java.sql.Date;

/**
 * Created by alxdaly on 4/10/2017.
 */

public class SearchJobObject {
    private int jobId;
    private Date date;

    public SearchJobObject(int jobId, Date date){
        this.jobId = jobId;
        this.date = date;
    }

    public int getJobId() {
        return jobId;
    }

    public Date getDate() {
        return date;
    }
}
