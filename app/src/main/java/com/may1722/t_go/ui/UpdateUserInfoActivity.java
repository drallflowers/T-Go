package com.may1722.t_go.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.may1722.t_go.R;
import com.may1722.t_go.model.PasswordEncrypter;
import com.may1722.t_go.networking.SaltRequest;

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


public class UpdateUserInfoActivity extends AppCompatActivity {

    private String userID;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        userID = getIntent().getExtras().getString("userID");
        username = getIntent().getExtras().getString("username");
        new AsyncGetUserInfo().execute(userID);
    }

    public void submit(View view){
        EditText email = (EditText) findViewById(R.id.emailText);
        EditText phone = (EditText) findViewById(R.id.phoneNumberText);
        EditText oldPassword = (EditText) findViewById(R.id.oldPasswordText);
        EditText newPassword = (EditText) findViewById(R.id.newPasswordText);
        String emailString = email.getText().toString();
        String phoneString = phone.getText().toString();
        String oldPasswordString = oldPassword.getText().toString();
        String newPasswordString = newPassword.getText().toString();
        SaltRequest saltRequest = new SaltRequest();
        saltRequest.execute(username);

        try {
            Thread.sleep(500);
            PasswordEncrypter passwordEncrypter = new PasswordEncrypter(saltRequest.getSalt());
            oldPasswordString = passwordEncrypter.hash(oldPasswordString);
            passwordEncrypter = new PasswordEncrypter(saltRequest.getSalt());
            newPasswordString = passwordEncrypter.hash(newPasswordString);
            if(oldPasswordString.length() == 0){
                Toast.makeText(UpdateUserInfoActivity.this, "Enter your password", Toast.LENGTH_LONG).show();
            }else if(emailString.length() == 0) {
                Toast.makeText(UpdateUserInfoActivity.this, "Enter your email", Toast.LENGTH_LONG).show();
            }else if(phoneString.length() == 0){
                Toast.makeText(UpdateUserInfoActivity.this, "Enter your phone number", Toast.LENGTH_LONG).show();
            }else if(newPasswordString.length() == 0){
                new AsyncUpdateUserInfo().execute(userID, emailString, phoneString, oldPasswordString, oldPasswordString);
            }else {
                new AsyncUpdateUserInfo().execute(userID, emailString, phoneString, oldPasswordString, newPasswordString);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

                    return result.toString();
                    // Pass data to onPostExecute method


                } else {
                    Toast.makeText(UpdateUserInfoActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(UpdateUserInfoActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("connection failure")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(UpdateUserInfoActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                updateInfo(result);
            }
        }
    }

    public void updateInfo(String result) {
        String[] result2 = new String[10];
        result2 = result.split(",");
        EditText email = (EditText) findViewById(R.id.emailText);
        email.setText(result2[3]);
        EditText phone = (EditText) findViewById(R.id.phoneNumberText);
        phone.setText(result2[4]);
    }

    private class AsyncUpdateUserInfo extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/updateuserinfo.php");

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
                        .appendQueryParameter("userid", params[0])
                        .appendQueryParameter("email", params[1])
                        .appendQueryParameter("phone_number", params[2])
                        .appendQueryParameter("old_password", params[3])
                        .appendQueryParameter("new_password", params[4]);
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
                    Toast.makeText(UpdateUserInfoActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(UpdateUserInfoActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                return "connection failure";
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            if (result.equalsIgnoreCase("connection failure")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Toast.makeText(UpdateUserInfoActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(UpdateUserInfoActivity.this, "Info Updated", Toast.LENGTH_LONG).show();
                String[] result2 = new String[10];
                result2 = result.split(",");
                Intent intent = new Intent(UpdateUserInfoActivity.this, ProfileActivity.class);
                intent.putExtra("userID", result2[0]);
                intent.putExtra("first_name", result2[1]);
                intent.putExtra("last_name", result2[2]);
                intent.putExtra("email", result2[3]);
                intent.putExtra("phone_number", result2[4]);
                intent.putExtra("user_type", result2[5]);
                intent.putExtra("date_created", result2[6]);
                intent.putExtra("date_of_birth", result2[7]);
                intent.putExtra("username", result2[8]);
                startActivity(intent);
                UpdateUserInfoActivity.this.finish();
            }
        }
    }

}
