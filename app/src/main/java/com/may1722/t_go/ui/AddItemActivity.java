package com.may1722.t_go.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateTimePatternGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.*;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.*;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.*;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.may1722.t_go.R;
import com.may1722.t_go.model.ProductObject;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddItemActivity extends AppCompatActivity {

    private TextView name;
    private TextView description;
    private TextView price;
    private TextView quantity;
    private Integer jobID;
    private String userID;
    private Integer productID = 0;

    private AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        userID = getIntent().getExtras().getString("userID");
        jobID = getIntent().getExtras().getInt("jobID");
    }

    public void submit(View view) throws JSONException {
        name = (TextView) findViewById(R.id.nameText);
        description = (TextView) findViewById(R.id.descriptionText);
        price = (TextView) findViewById(R.id.priceText);
        quantity = (TextView) findViewById(R.id.quantityText);

        String nameString = name.getEditableText().toString();
        String descriptionString = description.getEditableText().toString();
        String priceString = price.getEditableText().toString();
        String quantityString = quantity.getEditableText().toString();

        if (nameString.length() > 0 && priceString.length() > 0 && quantityString.length() > 0) {
            new AsyncAddItem().execute(nameString, descriptionString, priceString, quantityString, jobID.toString(), productID.toString());
        } else {
            Toast.makeText(AddItemActivity.this, "Missing info", Toast.LENGTH_LONG).show();
        }
    }

    private class AsyncAddItem extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/additem.inc.php");

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
                        .appendQueryParameter("item_name", params[0])
                        .appendQueryParameter("item_description", params[1])
                        .appendQueryParameter("price", params[2])
                        .appendQueryParameter("quantity", params[3])
                        .appendQueryParameter("job_id", params[4])
                        .appendQueryParameter("product_id", params[5]);
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
                    //Toast.makeText(AddItemActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                    return "connection failure";
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddItemActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddItemActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                startProfileActivity();
                Toast.makeText(AddItemActivity.this, "Item Added", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void startProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    //PRODUCT FETCHING
    public void getProducts(View view){
        alertBuilder = new AlertDialog.Builder(AddItemActivity.this);
        alertBuilder.setTitle("Select a product:");

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialog, int which){
               dialog.dismiss();
           }
        });

        new AsyncGetProducts().execute();
    }

    public class ProductAdapter extends ArrayAdapter<ProductObject> {

        public ProductAdapter(Context context, ArrayList<ProductObject> products) {
            super(context, 0, products);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProductObject product = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.select_dialog_singlechoice, parent, false);
            }

            TextView text = (TextView) convertView.findViewById(android.R.id.text1);
            text.setText(product.getProduct_name());

            return convertView;
        }

    }

    private class AsyncGetProducts extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/getproducts.php");

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
                Uri.Builder builder = new Uri.Builder();
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write("");
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
                Toast.makeText(AddItemActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddItemActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    addItemsToList(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddItemActivity.this, "OOPs! Something went wrong. Bad Results Returned.", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(JobBoardActivity.this, "Getting there", Toast.LENGTH_LONG).show();

            }
        }


    }

    public void addItemsToList(String result) throws JSONException {
        JSONArray jArray = new JSONArray(result);

        ArrayList<ProductObject> products = new ArrayList<>();
        for(int i=0; i<jArray.length(); i++){
            JSONObject jObject = jArray.getJSONObject(i);
            int id = jObject.getInt("product_id");
            String name = jObject.getString("product_name");
            double price = jObject.getDouble("avg_price");
            products.add(new ProductObject(id, name, price));
        }

        final ProductAdapter adapter = new ProductAdapter(this, products);

        alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                ProductObject product = adapter.getItem(which);
                productID = product.getProduct_id();
                name = (TextView) findViewById(R.id.nameText);
                description = (TextView) findViewById(R.id.descriptionText);
                price = (TextView) findViewById(R.id.priceText);
                name.setText(product.getProduct_name());
                description.setText(product.getProduct_description());
                price.setText(String.format("%.2f", product.getAvg_price()));

                dialog.dismiss();
            }
        });
        alertBuilder.show();
    }
}

