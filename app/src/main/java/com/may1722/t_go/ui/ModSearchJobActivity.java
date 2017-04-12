package com.may1722.t_go.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.may1722.t_go.R;
import com.may1722.t_go.model.SearchJobAdapter;
import com.may1722.t_go.model.SearchJobObject;

import java.util.ArrayList;

public class ModSearchJobActivity extends AppCompatActivity {

    private String userID;
    private ArrayList<SearchJobObject> jobs;
    private ListView jobListView;
    private SearchJobAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_search_job);
        userID = getIntent().getExtras().getString("userID");
        jobs = new ArrayList<SearchJobObject>();
        jobListView = (ListView) findViewById(R.id.jobListView);
        adapter = new SearchJobAdapter(this, jobs);
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

        }
    }
}
