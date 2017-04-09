package com.may1722.t_go.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.may1722.t_go.R;
import com.may1722.t_go.model.ItemObject;
import com.may1722.t_go.networking.ChatInfoRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.text.DecimalFormat;
import java.util.ArrayList;

public class JobCompleteActivity extends AppCompatActivity {
    private int my_ID;
    private int their_ID;
    private String their_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_complete);
        my_ID = getIntent().getExtras().getInt("my_id");
        their_ID = getIntent().getExtras().getInt("their_id");
        their_username = getIntent().getExtras().getString("their_username");
    }

    protected void goToProfile(View view)
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    protected void goToReview(View view)
    {
        Intent intent = new Intent(this, SubmitReviewActivity.class)
                .putExtra("my_ID", my_ID)
                .putExtra("their_ID", their_ID)
                .putExtra("their_username", their_username);
        startActivity(intent);
    }
}