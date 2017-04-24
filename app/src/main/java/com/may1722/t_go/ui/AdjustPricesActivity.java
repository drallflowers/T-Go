package com.may1722.t_go.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import java.util.List;

public class AdjustPricesActivity extends AppCompatActivity {
    private String jobId;
    private ArrayList<AdjustPriceItem> adjustPriceItems;
    private AdjustAdapter adapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_prices);
        listView = (ListView) findViewById(R.id.adjustItemListView);
        jobId = getIntent().getExtras().getString("jobID");
        new AsyncGetItems().execute(jobId);
    }

    private void addItems(String result) throws JSONException{
        JSONArray jArray = new JSONArray(result);

        adjustPriceItems = new ArrayList<AdjustPriceItem>();
        for(int i = 0; i < jArray.length(); i++) {
            JSONObject jObject = jArray.getJSONObject(i);
            Integer item_id = jObject.getInt("item_id");
            String name = jObject.getString("item_name");
            Double price = jObject.getDouble("price");
            Integer quantity = jObject.getInt("quantity");
            adjustPriceItems.add(new AdjustPriceItem(item_id, name, price, quantity));
        }

        adapter = new AdjustAdapter(this, adjustPriceItems);
        listView.setAdapter(adapter);
    }

    public void submitPrices(View view) {
        for(int i = 0; i < adjustPriceItems.size(); i++) {
            View itemView = listView.getChildAt(i);
            EditText pricePerItem = (EditText) itemView.findViewById(R.id.pricePerItemEditText);
            EditText quantity = (EditText) itemView.findViewById(R.id.quantityEditText);
            if(!(pricePerItem.getText().toString().equals(""))) {
                adjustPriceItems.get(i).setPrice(Double.parseDouble(pricePerItem.getText().toString()));
            }
            if(!(quantity.getText().toString().equals(""))) {
                adjustPriceItems.get(i).setQuantity(Integer.parseInt(quantity.getText().toString()));
            }
            AdjustPriceItem item = adjustPriceItems.get(i);
            new AsyncUpdateItem().execute(Integer.toString(item.getItem_id()), Double.toString(item.getPrice()), Integer.toString(item.getQuantity()), jobId);
        }
//        Intent intent = new Intent(this, ProfileActivity.class);
//        startActivity(intent);
        try {
            Thread.sleep(500);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class AdjustPriceItem {
        private Integer item_id;
        private String name;
        private Double price;
        private Integer quantity;

        public AdjustPriceItem(Integer item_id, String name, Double price, Integer quantity){
            this.item_id = item_id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public Integer getItem_id() {
            return item_id;
        }

        public String getName() {
            return name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    private class AdjustAdapter extends BaseAdapter {
        private Context context;
        private List<AdjustPriceItem> items;

        public AdjustAdapter(Context context, List<AdjustPriceItem> items) {
            this.context = context;
            this.items = items;
        }

        public int getCount(){
            return items.size();
        }

        @Override
        public AdjustPriceItem getItem(int pos) {
            return items.get(pos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final int pos = position;
            AdjustPriceItem i = items.get(position);
            if(convertView == null){
                holder = new ViewHolder();
                LayoutInflater mInflater = AdjustPricesActivity.this.getLayoutInflater();
                convertView = mInflater.inflate(R.layout.list_adjust_price_item, null);
                holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTextView);
                holder.pricePerItem = (EditText) convertView.findViewById(R.id.pricePerItemEditText);
                holder.quantity = (EditText) convertView.findViewById(R.id.quantityEditText);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
//                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//
//                convertView = mInflater.inflate(R.layout.list_adjust_price_item, null);
//                TextView itemTitle = (TextView) convertView.findViewById(R.id.itemTextView);
//                EditText pricePerItem = (EditText) convertView.findViewById(R.id.pricePerItemEditText);
//                EditText quantity = (EditText) convertView.findViewById(R.id.quantityEditText);
            }
            holder.ref = position;
            holder.itemTitle.setText(i.getName());
            holder.pricePerItem.setText(Double.toString(i.getPrice()));
            holder.pricePerItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals("")) {
                        items.get(pos).setPrice(Double.parseDouble(s.toString()));
                    }
                }
            });
            holder.quantity.setText(Integer.toString(i.getQuantity()));
            holder.quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals("")) {
                        items.get(pos).setQuantity(Integer.parseInt(s.toString()));
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView itemTitle;
            EditText pricePerItem;
            EditText quantity;
            int ref;
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
                Toast.makeText(AdjustPricesActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(AdjustPricesActivity.this, "OOPs! Something went wrong. Connection Problem." + result, Toast.LENGTH_LONG).show();
            } else {
                try {
                    addItems(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdjustPricesActivity.this, "OOPs! Something went wrong. Bad Results Returned.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class AsyncUpdateItem extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/updateitem.php");

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
                        .appendQueryParameter("item_id", params[0])
                        .appendQueryParameter("price", params[1])
                        .appendQueryParameter("quantity", params[2])
                        .appendQueryParameter("job_id", params[3]);
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
                Toast.makeText(AdjustPricesActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(AdjustPricesActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {

            }
        }
    }
}
