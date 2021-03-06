package com.may1722.t_go.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.may1722.t_go.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CurrentJobs extends ListActivity {

    private String userID;
    private String jobID;
    private int chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_jobs);

        userID = getIntent().getExtras().getString("userID");
        new AsyncGetMyJobs().execute(userID);
    }

    @Override
    public void onResume(){
        super.onResume();
        new AsyncGetMyJobs().execute(userID);
    }

    public class JobBoardCardData {
        public String location;
        public String username;
        public Integer userId;
        public String price; // String for mockup purposes, change to double later
        public String time; // String for mockup purposes, change to Date later
        public Integer jobID;
        public Integer chatID;

        public JobBoardCardData(String loc, String name, Integer userId, String price, String time, Integer jobID, Integer chatID){
            location = loc;
            username = name;
            this.userId = userId;
            this.price = price;
            this.time = time;
            this.jobID = jobID;
            this.chatID= chatID;
        }
    }

    public class JobBoardCardDataAdapter extends ArrayAdapter<JobBoardCardData> {

        public JobBoardCardDataAdapter(Context context, ArrayList<JobBoardCardData> jobs){
            super(context, 0, jobs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            JobBoardCardData job = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_job_board, parent, false);
            }
            TextView jobLocation = (TextView) convertView.findViewById(R.id.job_board_location);
            TextView jobUsername = (TextView) convertView.findViewById(R.id.job_board_username);
            TextView jobPrice = (TextView) convertView.findViewById(R.id.job_board_price);
            TextView jobTime = (TextView) convertView.findViewById(R.id.job_board_time);

            jobLocation.setText(job.location);
            jobUsername.setText(job.username);
            jobPrice.setText(job.price);
            jobTime.setText(job.time);

            return convertView;
        }

    }

    private class AsyncGetMyJobs extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/getmyjobs.inc.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("userid", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                    // Pass data to onPostExecute method


                } else {
                    //Toast.makeText(JobBoardActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(CurrentJobs.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("failed to get your jobs")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(CurrentJobs.this, result, Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("connection failure")){
                Toast.makeText(CurrentJobs.this, result, Toast.LENGTH_LONG).show();
            }else {
                addItemsToList(result);
                //Toast.makeText(JobBoardActivity.this, "Getting there", Toast.LENGTH_LONG).show();

            }
        }


    }

    public void addItemsToList(String result){
        List<String> myList = new ArrayList<String>(Arrays.asList(result.split("<br>")));
        ArrayList<JobBoardCardData> jobs = new ArrayList<>();

        if(result.length() > 0) {
            for (String item : myList) {
                List<String> jobList = new ArrayList<String>(Arrays.asList(item.split(", ")));
                jobs.add(new JobBoardCardData(jobList.get(0), jobList.get(1), Integer.parseInt(jobList.get(5)), jobList.get(2), jobList.get(3), Integer.parseInt(jobList.get(4)), Integer.parseInt(jobList.get(6))));
            }
        }else{
            Toast.makeText(CurrentJobs.this, "You have no jobs", Toast.LENGTH_LONG).show();
        }

        JobBoardCardDataAdapter adapter = new JobBoardCardDataAdapter(this, jobs);
        ListView list = this.getListView();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JobBoardCardData selected = (JobBoardCardData) parent.getAdapter().getItem(position);
                //new AsyncCompleteJob().execute(selected.jobID.toString());

                jobID = selected.jobID.toString();
                chatID = selected.chatID;
                goToJobDetails();
            }
        });
    }

    private class AsyncCompleteJob extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/completejob.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("job_id", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                    // Pass data to onPostExecute method


                } else {
                    //Toast.makeText(JobBoardActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(CurrentJobs.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("connection failure")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(CurrentJobs.this, result, Toast.LENGTH_LONG).show();
            }else {
                //addItemsToList(result);
                Toast.makeText(CurrentJobs.this, "completed", Toast.LENGTH_LONG).show();

            }
        }


    }

    //SUBMITTING PRICES
    public void goToJobDetails() {
        Intent intent = new Intent(this, JobDetailsActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("job_ID", jobID);
        intent.putExtra("chat_ID", chatID);
        intent.putExtra("from_where", "current_jobs");
        startActivity(intent);
    }

}
