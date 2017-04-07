package com.may1722.t_go.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.may1722.t_go.R;
import com.may1722.t_go.model.PasswordEncrypter;

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

public class SignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = (EditText) findViewById(R.id.UserNameEditText);
        password = (EditText) findViewById(R.id.PasswordEditText);
        confirm_password = (EditText) findViewById(R.id.ConfirmPasswordEditText);
        setContentView(R.layout.activity_signup);
    }

    /*
     * On cancel, head back to login without touching database
     */
    public void goToLogin(View view) {

        //Sets up the class of the Activity we want to go to
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /*
    * On submit, send information to DB  after checking fields and change screen to login
    */
    public void signUp(View view) {
        //Database insert & head to login
        TextView userField = (TextView) findViewById(R.id.UserNameEditText);
        TextView passField = (TextView) findViewById(R.id.PasswordEditText);
        TextView confirmPassField = (TextView) findViewById(R.id.ConfirmPasswordEditText);

        String username = userField.getText().toString();
        String password = passField.getText().toString();
        String confirmPassword = confirmPassField.getText().toString();
        if (password.equals(confirmPassword) && password.length() > 0 && username.length() > 0) {
            PasswordEncrypter passwordEncrypter = new PasswordEncrypter();
            password = passwordEncrypter.hash(password);
            int salt = passwordEncrypter.getSalt();
            new AsyncSignUp().execute(username, password, Integer.toString(salt));
        } else {
            //error message
            Toast.makeText(SignUpActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
        }

    }

    private class AsyncSignUp extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/signup.inc.php");

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
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1])
                        .appendQueryParameter("salt", params[2]);
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
                    Toast.makeText(SignUpActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SignUpActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(SignUpActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("Username taken")) {
                Toast.makeText(SignUpActivity.this, "Username is taken", Toast.LENGTH_LONG).show();
            } else {
                String[] result2 = new String[10];
                result2 = result.split(",");
                Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
                intent.putExtra("userid", result2[0]);
                intent.putExtra("first_name", result2[1]);
                intent.putExtra("last_name", result2[2]);
                intent.putExtra("email", result2[3]);
                intent.putExtra("phone_number", result2[4]);
                intent.putExtra("user_type", result2[5]);
                intent.putExtra("date_created", result2[6]);
                intent.putExtra("date_of_birth", result2[7]);
                intent.putExtra("username", result2[8]);
                startActivity(intent);
                SignUpActivity.this.finish();

            }
        }
    }
}
