package com.may1722.t_go.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.may1722.t_go.R;
import com.may1722.t_go.model.SearchJobAdapter;
import com.may1722.t_go.model.SearchJobObject;
import com.may1722.t_go.networking.SearchJobRequest;

import java.util.ArrayList;

public class ModSearchJobActivity extends AppCompatActivity {

    private String userID;
    private ArrayList<SearchJobObject> jobs;
    private ListView jobListView;
    private SearchJobAdapter adapter;
    private SearchJobRequest searchJobRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_search_job);
        userID = getIntent().getExtras().getString("userID");
        jobs = new ArrayList<SearchJobObject>();
        jobListView = (ListView) findViewById(R.id.jobListView);
        adapter = new SearchJobAdapter(ModSearchJobActivity.this, this, jobs);
        jobListView.setAdapter(adapter);
    }

    public void searchJobs(View view){
        TextView userTextView = (TextView) findViewById(R.id.MSJuserInput);
        TextView courierTextView = (TextView) findViewById(R.id.MSJcourierInput);
        String userName = userTextView.getText().toString();
        String courierName = courierTextView.getText().toString();

        if(userName.length() == 0){
            Toast.makeText(ModSearchJobActivity.this, "Please submit the user's username.", Toast.LENGTH_LONG).show();
        }
        else if(courierName.length() == 0){
            Toast.makeText(ModSearchJobActivity.this, "Please submit the courier's username.", Toast.LENGTH_LONG).show();
        }
        else{
            searchJobRequest = new SearchJobRequest();
            searchJobRequest.execute(userName, courierName);

            try {
                Thread.sleep(500);
                jobs = searchJobRequest.getJobs();
                adapter = new SearchJobAdapter(ModSearchJobActivity.this, this, jobs);
                jobListView.setAdapter(adapter);
                if(jobs.size() <= 0){
                    Toast.makeText(ModSearchJobActivity.this, "No jobs found.alx", Toast.LENGTH_LONG).show();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void openJobDetailsPage(int position){
        Intent intent = new Intent(this, JobDetailsActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("job_ID", Integer.toString(jobs.get(position).getJobId()));
        intent.putExtra("chatID", -1);
        intent.putExtra("from_where", "search_job");
        startActivity(intent);

    }

}
