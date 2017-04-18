package com.may1722.t_go.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.vision.text.Text;
import com.may1722.t_go.R;
import com.may1722.t_go.model.ItemObject;
import com.may1722.t_go.networking.ChatInfoRequest;
import com.may1722.t_go.networking.CreateChatRequest;

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
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class JobDetailsActivity extends ListActivity {

    static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;
    private String userID;
    private String otherID;
    private String jobID;
    private String token;
    private String requestorid;
    private String courierid;
    private String jobCode;
    private String fromWhere;
    private int chatId;
    private double totalPrice;
    private String username;
    private String othername;
    private  ArrayList<JobDetailsActivity.ItemCardData> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        userID = getIntent().getExtras().getString("userID");
        otherID = "";
        totalPrice = 0.0;
        jobID = getIntent().getExtras().getString("job_ID");
        chatId = getIntent().getExtras().getInt("chat_ID");
        fromWhere = getIntent().getExtras().getString("from_where");
        if(fromWhere.equals("job_board")){
            Button chat = (Button) findViewById(R.id.chatButton);
            chat.setVisibility(View.GONE);
        }
        new AsyncGetJobInfo().execute(jobID);

    }

    public void acceptJob(View view) {
        new AsyncClaimJob().execute(userID, jobID);
        CreateChatRequest createChatRequest = new CreateChatRequest();
        createChatRequest.execute(userID, otherID, jobID);

        try {
            Thread.sleep(500);
            chatId = createChatRequest.getChatId();Button chat = (Button) findViewById(R.id.chatButton);
            Button acceptJob = (Button) findViewById(R.id.acceptJobButton);
            chat.setVisibility(View.VISIBLE);
            acceptJob.setVisibility(View.GONE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void completeJob(View view) {

        jobCode = jobCode();

        if(requestorid.equals(userID)){
            Toast.makeText(JobDetailsActivity.this, "Verification Code: \n"+jobCode, Toast.LENGTH_LONG).show();
        } else if(courierid.equals(userID)) {
            LayoutInflater inflater = LayoutInflater.from(JobDetailsActivity.this);
            final View dialogView = inflater.inflate(R.layout.edit_dialog, null);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(JobDetailsActivity.this);
            alertBuilder.setTitle("Enter Verification Code: ");
            alertBuilder.setView(dialogView);

            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                }
            });

            alertBuilder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText entry = (EditText)dialogView.findViewById(R.id.edit_dialog_input);
                    String userEntry = entry.getText().toString();
                    if(userEntry.equals(jobCode)){
                        new AsyncCompleteJob().execute(jobID, Double.toString(totalPrice));
                        System.out.println(token);
                        System.out.println(((int)(totalPrice*100))+"");
                        new AsyncSendPayment().execute(token,((int)(totalPrice*100))+"");
                        dialogInterface.dismiss();
                        goToJobComplete();
                    } else {
                        Toast.makeText(JobDetailsActivity.this, "Incorrect Verification Code", Toast.LENGTH_LONG).show();
                    }
                }
            });

            alertBuilder.show();

        } else {
            Toast.makeText(JobDetailsActivity.this, "How did you get here?", Toast.LENGTH_LONG).show();
        }
    }

    public void goToJobComplete(){
        Intent intent = new Intent(this, JobCompleteActivity.class)
                .putExtra("job_id", jobID);
        startActivity(intent);
    }

    public void goToMaps(View view) {
        findPlace(view);

    }

    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT).build())
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Uri uri = Uri.parse("google.navigation:q=" + place.getLatLng().latitude + "," + place.getLatLng().longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public class ItemCardData {
        public String item_name;
        public String item_description;
        public String price; // String for mockup purposes, change to double later
        public String quantity; // String for mockup purposes, change to Date later
        public Integer itemID;

        public ItemCardData(String name, String description, String price, String quantity, Integer itemID) {
            item_name = name;
            item_description = description;
            this.price = price;
            this.quantity = quantity;
            this.itemID = itemID;
        }
    }

    public class ItemCardDataAdapter extends ArrayAdapter<JobDetailsActivity.ItemCardData> {

        public ItemCardDataAdapter(Context context, ArrayList<JobDetailsActivity.ItemCardData> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            JobDetailsActivity.ItemCardData item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_details, parent, false);
            }
            TextView item_name = (TextView) convertView.findViewById(R.id.item_name);
            TextView item_description = (TextView) convertView.findViewById(R.id.item_description);
            TextView item_price = (TextView) convertView.findViewById(R.id.item_price);
            TextView item_quantity = (TextView) convertView.findViewById(R.id.item_quantity);

            item_name.setText(item.item_name);
            item_description.setText(item.item_description);
            item_price.setText(item.price);
            item_quantity.setText(item.quantity);

            return convertView;
        }

    }

    public void addItemsToList(String result) throws JSONException {
        JSONArray jArray = new JSONArray(result);

         itemsList = new ArrayList<>();
        for (int i = 0; i < jArray.length(); i++) {
            //Add item object
            JSONObject jObject = jArray.getJSONObject(i);
            Integer item_id = jObject.getInt("item_id");
            String name = jObject.getString("item_name");
            String description = jObject.getString("item_description");
            Double price = jObject.getDouble("price");
            Integer count = jObject.getInt("quantity");
            Integer product_id = jObject.getInt("product_id");
            totalPrice += (price*count);
            itemsList.add(new JobDetailsActivity.ItemCardData(name, description, price.toString(), count.toString(), product_id));
        }

        ItemCardDataAdapter adapter = new ItemCardDataAdapter(this, itemsList);
        ListView list = this.getListView();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JobDetailsActivity.ItemCardData selected = (JobDetailsActivity.ItemCardData) parent.getAdapter().getItem(position);
                //new AsyncClaimJob().execute(userID, selected.jobID.toString());
                //goToJobDetails(selected.jobID.toString());
                //Toast.makeText(JobBoardActivity.this, "claimed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goToChat(View view)
    {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user1", username);
        intent.putExtra("user2", othername);
        intent.putExtra("chatID", chatId);
        startActivity(intent);
    }


    private class AsyncClaimJob extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/claimjob.inc.php");

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
                        .appendQueryParameter("job_id", params[1]);
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
                Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(JobDetailsActivity.this, "claimed", Toast.LENGTH_LONG).show();
                new AsyncGetJobInfo().execute(jobID);
            }
        }


    }


    private class AsyncGetJobInfo extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/getjobinfo.php");

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
                    //Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                addJobDetails(result);
                //Toast.makeText(JobDetailsActivity.this, "success", Toast.LENGTH_LONG).show();
            }
        }


    }

    public void addJobDetails(String result) {
        String[] result2 = new String[10];
        result2 = result.split(",");
        TextView requestor = (TextView) findViewById(R.id.jobRequestor);
        TextView location = (TextView) findViewById(R.id.jobLocation);
        TextView instructions = (TextView) findViewById(R.id.jobInstructionsLabel);
        TextView jobStatus = (TextView) findViewById(R.id.jobStatus);
        TextView timeposted = (TextView) findViewById(R.id.timePostedLabel);
        TextView timedue = (TextView) findViewById(R.id.timeDueLabel);
        TextView courier = (TextView) findViewById(R.id.jobCourier);

        requestorid = result2[2];
        courierid = result2[3];

        if (requestorid.equals(userID)) {
            if (result2[6].equals("1")) {
                Button completeJob = (Button) findViewById(R.id.completeJobButton);
                completeJob.setVisibility(View.VISIBLE);
            }
            requestor.setText("Requestor: " + result2[0] + " (you)");
            username = result2[0];
        } else {
            if (result2[6].equals("0")) {
                Button acceptJob = (Button) findViewById(R.id.acceptJobButton);
                acceptJob.setVisibility(View.VISIBLE);
            }
            requestor.setText("Requestor: " + result2[0]);
            otherID = requestorid;
            othername = result2[0];
        }

        if (courierid.equals(userID)) {
            if (result2[6].equals("1")) {
                Button completeJob = (Button) findViewById(R.id.completeJobButton);
                completeJob.setVisibility(View.VISIBLE);
            }
            courier.setText("Courier: " + result2[1] + " (you)");
            username = result2[1];
        } else {
            courier.setText("Courier: " + result2[1]);
            if(!otherID.equals(requestorid)){
                otherID = courierid;
                othername = result2[1];
            }
        }

        location.setText(result2[4]);
        instructions.setText(result2[5]);
        jobStatus.setText("Job Status: " + result2[6]);
        timeposted.setText("Time Posted: " + result2[7]);
        timedue.setText("Time Due: " + result2[8]);
        token = result2[9];
        new AsyncGetItems().execute(jobID);
    }

    private static class AsyncSendPayment extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/payment.php");

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

                String key = "Bar12345Bar12345"; // 128 bit key
                // Create key and cipher
                Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
                String decrypted = "";
                try {
                    Cipher cipher = Cipher.getInstance("AES");
                    cipher.init(Cipher.DECRYPT_MODE, aesKey);
                    decrypted= new String(cipher.doFinal(params[0].getBytes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(decrypted);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("stripeToken",decrypted).appendQueryParameter("price",params[1]);
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
            } else {
                System.out.println(result);

            }
        }


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
                Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem." + result, Toast.LENGTH_LONG).show();
            } else {
                try {
                    addItemsToList(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Bad Results Returned.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class AsyncCompleteJob extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/completejob.php");

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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("job_id", params[0])
                        .appendQueryParameter("payment_amount", params[1]);
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
                    //Toast.makeText(JobBoardActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(JobDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobDetailsActivity.this, result, Toast.LENGTH_LONG).show();
            } else {
//                new JobDetailsActivity.AsyncGetJobInfo().execute(jobID);
                Toast.makeText(JobDetailsActivity.this, "completed", Toast.LENGTH_LONG).show();

            }
        }
    }

    /**
     * Calculates a code based on job information.  Used for the verification between courier and requester
     * @return
     */
    private String jobCode(){
        TextView requester = (TextView) findViewById(R.id.jobRequestor);
        TextView courier = (TextView) findViewById(R.id.jobCourier);
        TextView jobTime = (TextView) findViewById(R.id.timePostedLabel);

        int code = requester.getText().toString().replace(" (you)", "").hashCode()
                + courier.getText().toString().replace(" (you)", "").hashCode()
                + jobTime.getText().toString().hashCode();

        int shortCode = Math.abs(code%1000000);

        return String.format("%06d", shortCode);
    }
}
