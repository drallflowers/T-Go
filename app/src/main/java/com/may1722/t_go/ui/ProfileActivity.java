package com.may1722.t_go.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.may1722.t_go.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void goToCurrentJobs(View view){
        Intent intent = new Intent(this, CurrentJobs.class);
        startActivity(intent);
    }

    public void goToJobSubmit(View view){
        Intent intent = new Intent(this, JobSubmitActivity.class);
        startActivity(intent);
    }

    public void goToReviewBoard(View view){
        Intent intent = new Intent(this, ReviewBoardActivity.class);
        startActivity(intent);
    }

    public void goToJobBoard(View view){
        Intent intent = new Intent(this, JobBoardActivity.class);
        startActivity(intent);
    }
}
