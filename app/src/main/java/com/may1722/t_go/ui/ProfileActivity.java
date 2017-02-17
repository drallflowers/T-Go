package com.may1722.t_go.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.may1722.t_go.R;

import org.w3c.dom.Text;

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

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String username = getIntent().getExtras().getString("username");
        String user_type = getIntent().getExtras().getString("user_type");
        String email = getIntent().getExtras().getString("email");
        String phone_number =  getIntent().getExtras().getString("phone_number");
        TextView profiletextView = (TextView) findViewById(R.id.profileUsername);
        TextView typetextView = (TextView) findViewById(R.id.profileUserLevel);
        TextView emailtextView = (TextView) findViewById(R.id.profileEmail);
        TextView phonetextView = (TextView) findViewById(R.id.profilePhoneLabel);
        profiletextView.setText(username);
        typetextView.setText(user_type);
        emailtextView.setText(email);
        phonetextView.setText(phone_number);


    }

    public void goToCurrentJobs(View view) {
        Intent intent = new Intent(this, CurrentJobs.class);
        startActivity(intent);
    }

    public void goToJobSubmit(View view) {
        Intent intent = new Intent(this, JobSubmitActivity.class);
        startActivity(intent);
    }

    public void goToReviewBoard(View view) {
        Intent intent = new Intent(this, ReviewBoardActivity.class);
        startActivity(intent);
    }

    public void goToJobBoard(View view) {
        Intent intent = new Intent(this, JobBoardActivity.class);
        startActivity(intent);
    }

}
