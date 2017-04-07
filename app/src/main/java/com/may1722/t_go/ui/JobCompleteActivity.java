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

    private ArrayList<EditText> entries;
    private ArrayList<ItemObject> items;
    private ChatInfoRequest chatInfoRequest;
    private int chatId;
    private int userId;
    private String username;
    private String othername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_complete);

        String jobID = getIntent().getExtras().getString("job_id");
        chatId = getIntent().getExtras().getInt("chat_id");
        userId = getIntent().getExtras().getInt("user_id");
        chatInfoRequest = new ChatInfoRequest();
        chatInfoRequest.execute(Integer.toString(chatId), Integer.toString(userId));
        new AsyncGetItems().execute(jobID);

    }

    private class AsyncGetItems extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/getjobitems.php");

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
                        .appendQueryParameter("job_id", params[0]);
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
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(JobCompleteActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobCompleteActivity.this, "OOPs! Something went wrong. Connection Problem."+result, Toast.LENGTH_LONG).show();
            } else {
                try {
                    addItemsToView(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(JobCompleteActivity.this, "OOPs! Something went wrong. Bad Results Returned.", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void addItemsToView(String result) throws JSONException{
        LinearLayout contentPane = (LinearLayout) findViewById(R.id.content_pane);

        JSONArray jArray = new JSONArray(result);

        items = new ArrayList<>();
        entries = new ArrayList<>();
        for(int i=0; i<jArray.length(); i++){
            //Add item object
            JSONObject jObject = jArray.getJSONObject(i);
            int item_id = jObject.getInt("item_id");
            String name = jObject.getString("item_name");
            double price = jObject.getDouble("price");
            int count = jObject.getInt("quantity");
            int product_id = jObject.getInt("product_id");

            items.add(new ItemObject(item_id, name, price, count, product_id));

            //Add accompanying edittext
            EditText eText = new EditText(this);
            eText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            eText.setHint(name);
            eText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            entries.add(eText);
            contentPane.addView(eText);
        }
    }

    public void submit(View view){

        for(int i=0; i<entries.size(); i++){
            EditText editText = entries.get(i);
            if(!TextUtils.isEmpty(editText.getText())){
                ItemObject assocItem = items.get(i);
                int productID = assocItem.getProduct_id();
                double price = Double.parseDouble(editText.getText().toString());

                if(assocItem.getProduct_id() > 0) {
                    new AsyncUpdatePrice().execute(Integer.toString(productID), Double.toString(price));
                } else {
                    new AsyncAddProduct().execute(assocItem.getItem_name(), assocItem.getItem_description(), Double.toString(price));
                }
            }
        }

        finish();
    }

    private class AsyncUpdatePrice extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/updateproduct.php");

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
                        .appendQueryParameter("product_id", params[0])
                        .appendQueryParameter("price", params[1]);
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
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(JobCompleteActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobCompleteActivity.this, "OOPs! Something went wrong. Connection Problem."+result, Toast.LENGTH_LONG).show();
            } else {
            }
        }
    }

    public void goToChat(View view)
    {
        username = chatInfoRequest.getUser1();
        othername = chatInfoRequest.getUser2();
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user1", username);
        intent.putExtra("user2", othername);
        intent.putExtra("chatId", chatId);
        startActivity(intent);
    }

    private class AsyncAddProduct extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/addproduct.php");

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
                        .appendQueryParameter("product_name", params[0])
                        .appendQueryParameter("product_description", params[1])
                        .appendQueryParameter("avg_price", params[2]);
                String query = builder.build().getEncodedQuery();
                System.out.println(query);
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
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(JobCompleteActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobCompleteActivity.this, "OOPs! Something went wrong. Connection Problem."+result, Toast.LENGTH_LONG).show();
            } else {
            }
        }
    }
}