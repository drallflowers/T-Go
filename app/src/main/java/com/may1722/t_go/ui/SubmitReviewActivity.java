package com.may1722.t_go.ui;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class SubmitReviewActivity extends AppCompatActivity {
    private String my_ID;
    private String their_ID;
    private EditText details;
    private float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_review);
        my_ID = getIntent().getExtras().getString("my_id");
        their_ID = getIntent().getExtras().getString("their_id");
        String their_username = getIntent().getExtras().getString("their_username");
        details = (EditText) findViewById(R.id.reviewDetails);

        TextView reviewee = (TextView)findViewById(R.id.review_target);
        reviewee.setText(their_username);

        RatingBar rating = (RatingBar) findViewById(R.id.reviewRating);
        this.rating = rating.getRating();

    }

    public void submitReview(View view)
    {
        String detailString = details.getText().toString();
        RatingBar rating = (RatingBar) findViewById(R.id.reviewRating);
        this.rating = rating.getRating();
        new AsyncSubmitReview().execute(detailString);
        finish();
    }

    private class AsyncSubmitReview extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/submitReview.php");

            } catch (MalformedURLException e) {
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
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_ID", my_ID)
                        .appendQueryParameter("their_ID", their_ID)
                        .appendQueryParameter("details", params[0])
                        .appendQueryParameter("rating", String.valueOf(rating));
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
                    //Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SubmitReviewActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            Toast.makeText(SubmitReviewActivity.this, result, Toast.LENGTH_LONG).show();
            /*if (result.equalsIgnoreCase("connection failure")) {
                *//* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 *//*
                Toast.makeText(SubmitReviewActivity.this, "Failure", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SubmitReviewActivity.this, "Success", Toast.LENGTH_LONG).show();
            }*/
        }


    }
}
