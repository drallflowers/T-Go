package com.may1722.t_go.networking;

import android.net.Uri;

import com.may1722.t_go.model.SearchJobObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alxdaly on 4/11/2017.
 */

public class SearchJobRequest extends Request {
    ArrayList<SearchJobObject> jobs;
    String[] params;

    /**
     * Set up URL to use chat.php
     *
     * @return
     */
    protected URL getUrl() {
        try {
            URL url = new URL("http://may1722db.ece.iastate.edu/searchjob.php");
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set as GET
     *
     * @return
     */
    protected String getPostGet(){
        return "GET";
    }

    /**
     * Build Uri.Builder to make php call
     *
     * @param params
     * @return
     */
    protected Uri.Builder getUriBuilder(String... params){
        this.params = params;
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("userName", params[0])
                .appendQueryParameter("courierName", params[1]);
        return builder;
    }

    /**
     * Handle the results of running chat.php
     *
     * @param result
     */
    protected void handleResult(String result){
        if(result.equalsIgnoreCase("Error: Username(s) not correct.")){

        }
        else{
            try {
                JSONArray jsonArray = new JSONArray(result);
                jobs = new ArrayList<SearchJobObject>();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String jobDate = object.getString("job_date");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdf.parse(jobDate);
                        SearchJobObject newJob = new SearchJobObject(object.getInt("job_id"), date);
                        jobs.add(newJob);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<SearchJobObject> getJobs(){
        return jobs;
    }
}
