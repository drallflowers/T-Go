package com.may1722.t_go.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.may1722.t_go.R;

public class JobCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_complete);

        Intent intent = getIntent();
        Integer jobID = intent.getIntExtra("job_id", -1);



    }
}
