package com.may1722.t_go.ui;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.may1722.t_go.R;

public class CurrentJobs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_jobs);
    }

    public void goToReviewSubmit(View view){
        Intent intent = new Intent(this, SubmitReviewActivity.class);
        startActivity(intent);
    }

}
