package com.may1722.t_go.ui;

import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.may1722.t_go.R;

import java.util.ArrayList;
import java.util.Date;

public class JobBoardActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_board);

        //ArrayList to be replaced by calls to database after mockups
        ArrayList<JobBoardCardData> jobs = new ArrayList<>();
        jobs.add(new JobBoardCardData("Location/Distance", "Username", "Price", "Time"));
        jobs.add(new JobBoardCardData("Ames HyVee", "mild_apocalypse", "$8.99", "12:35 PM"));
        jobs.add(new JobBoardCardData("3.4 mi", "brief_armageddon", "$5.80", "1:01 PM"));
        jobs.add(new JobBoardCardData("123 Wheeler Street", "eric", "$0.95", "11:55 AM"));

        JobBoardCardDataAdapter adapter = new JobBoardCardDataAdapter(this, jobs);
        ListView list = this.getListView();
        list.setAdapter(adapter);
    }

    public class JobBoardCardData {
        public String location;
        public String username;
        public String price; // String for mockup purposes, change to double later
        public String time; // String for mockup purposes, change to Date later

        public JobBoardCardData(String loc, String name, String price, String time){
            location = loc;
            username = name;
            this.price = price;
            this.time = time;
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
}
