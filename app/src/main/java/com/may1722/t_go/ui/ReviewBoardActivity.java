package com.may1722.t_go.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.may1722.t_go.R;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewBoardActivity extends AppCompatActivity {

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_board);

        userID = getIntent().getExtras().getString("userID");
        new AsyncGetReviews().execute(userID);
    }

    public class Rating {
        public String username;
        public String details;
        public double rating;

        public Rating(String name, String description, double rate){
            username = name;
            details = description;
            rating = rate;
        }
    }

    public class RatingAdapter extends ArrayAdapter<Rating> {

        public RatingAdapter(Context context, ArrayList<Rating> ratings){
            super(context, 0, ratings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Rating rate = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review_board, parent, false);
            }

            TextView ratingUsername = (TextView)convertView.findViewById(R.id.review_board_item_username);
            TextView ratingDetails = (TextView)convertView.findViewById(R.id.review_board_item_details);
            TextView ratingRating = (TextView) convertView.findViewById(R.id.review_board_item_rating);

            ratingUsername.setText(rate.username);
            ratingDetails.setText(rate.details);
            ratingRating.setText(String.format("%.2f", rate.rating));

            if(rate.rating < 2){
                ratingRating.setTextColor(Color.RED);
            } else if(rate.rating < 3.5){
                ratingRating.setTextColor(Color.YELLOW);
            } else {
                ratingRating.setTextColor(Color.GREEN);
            }

            return convertView;
        }
    }

    private class AsyncGetReviews extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/getuserreview.php");

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
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", params[0]);
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
                    //Toast.makeText(JobSubmitActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ReviewBoardActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(ReviewBoardActivity.this, "No jobs available", Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("connection failure")) {
                Toast.makeText(ReviewBoardActivity.this, result, Toast.LENGTH_LONG).show();
            }else if(result.contains("Error")){
                Toast.makeText(ReviewBoardActivity.this, result, Toast.LENGTH_LONG).show();
            }else {
                try {
                    addItemsToList(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ReviewBoardActivity.this, "OOPs! Something went wrong. Bad Results Returned.", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(JobBoardActivity.this, "Getting there", Toast.LENGTH_LONG).show();

            }
        }


    }

    public void addItemsToList(String result) throws JSONException {
        List<String> myList = new ArrayList<String>(Arrays.asList(result.split("<br>")));

        ArrayList<Rating> ratings = new ArrayList<>();
        JSONArray jArray = new JSONArray(result);
        for(int i=0; i<jArray.length(); i++){
            JSONObject jObject = jArray.getJSONObject(i);
            int reviewee_id = jObject.getInt("reviewee_id");
            int reviewer_id = jObject.getInt("reviewer_id");
            String username = jObject.getString("reviewer");
            String description = jObject.getString("description");
            double rating = jObject.getDouble("rating");
            ratings.add(new Rating(username, description, rating));
        }

        RatingAdapter adapter = new RatingAdapter(this, ratings);
        ListView list = (ListView) findViewById(R.id.list_review_board);
        list.setAdapter(adapter);
    }

}
