package com.may1722.t_go.model;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.may1722.t_go.ui.LoginActivity;
import com.may1722.t_go.ui.ProfileActivity;
import com.may1722.t_go.ui.UpdateUserInfoActivity;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by David on 3/28/2017.
 */

public class MailObject {

    private int mail_id;
    private int user_id;
    private String username;
    private int user_type;
    private String content;
    private int mail_type;
    private int from_userid;
    private float rating;

    public MailObject(int id, int user, String name, int usertype, String c, int type, int fromid) {
        mail_id = id;
        user_id = user;
        username = name;
        user_type = usertype;
        content = c;
        mail_type = type;
        from_userid = fromid;
    }


    public int getMail_id() {
        return mail_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUserName() {
        return username;
    }

    public int getUserType() {
        return user_type;
    }

    public String getContent() {
        return content;
    }

    public int getMail_type() {
        return mail_type;
    }

    public int getFrom_userid() {
        return from_userid;
    }

    public float getRating() {
        try {
            int id = this.getFrom_userid();
            String str_result= new AsyncGetUserInfo().execute(String.valueOf(id)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rating;
    }

    private class AsyncGetUserInfo extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/getuserinfo.php");

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
                        .appendQueryParameter("userid", params[0]);
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

                    String[] result2 = new String[10];
                    result2 = result.toString().split(",");
                    rating = Float.parseFloat(result2[9]);

                    return result2[9];
                    // Pass data to onPostExecute method


                } else {
                    //Toast.makeText(UpdateUserInfoActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(UpdateUserInfoActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

    }
}
