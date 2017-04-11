package com.may1722.t_go.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.may1722.t_go.R;
import com.may1722.t_go.model.ListViewAdapter;
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
import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {

    private TextView name;
    private TextView description;
    private static TextView price;
    private TextView quantity;
    private Integer jobID;
    private String userID;
    private String user_type;
    private Integer productID = 0;

    static String itemName;
    static ListView listView;
    static ArrayList<String> listItems;
    static ArrayList<String> listDescription;
    static ArrayList<Double> listPrices;
    static ArrayList<Integer> listQuantity;
    static ListViewAdapter adapter;
    static TextView priceView;
    static double totalPrice;

    private AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        userID = getIntent().getExtras().getString("userID");
        user_type = getIntent().getExtras().getString("type");
        jobID = Integer.parseInt(getIntent().getExtras().getString("jobID"));


        price = (TextView) findViewById(R.id.totalPrice);
        listView = (ListView) findViewById(R.id.listView);

        // initialize objects
        totalPrice = 0.00;
        listItems = new ArrayList<>();
        listDescription = new ArrayList<>();
        listPrices = new ArrayList<>();
        listQuantity = new ArrayList<>();
        adapter = new ListViewAdapter(listItems, listDescription, listPrices,listQuantity, this);
        listView.setAdapter(adapter);
        itemName = "";
        priceView = (TextView) findViewById(R.id.totalPrice);

        setListViewHeightBasedOnChildren(listView);
        Button addItemBtn = (Button) findViewById(R.id.addItemBtn);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                DialogFragment newFragment = new CustomItemFragment();
                newFragment.show(getSupportFragmentManager(), "test");


            }
        });

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListViewAdapter listAdapter = (ListViewAdapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public void fragmentTaskCompleted()
    {
        setListViewHeightBasedOnChildren(listView);
        adapter.notifyDataSetChanged();
    }

    public void submit(View view) throws JSONException {
        /*name = (TextView) findViewById(R.id.nameText);
        description = (TextView) findViewById(R.id.descriptionText);
        price = (TextView) findViewById(R.id.priceText);
        quantity = (TextView) findViewById(R.id.quantityText);*/


        for(int i = 0; i < listItems.size(); i++){
            String nameString = listItems.get(i);
            String descriptionString = listDescription.get(i);
            String priceString = listPrices.get(i).toString();
            String quantityString =listQuantity.get(i).toString();

            if (nameString.length() > 0 && priceString.length() > 0 && quantityString.length() > 0) {
                new AsyncAddItem().execute(nameString, descriptionString, priceString, quantityString, jobID.toString(), productID.toString());
            } else {
                Toast.makeText(AddItemActivity.this, "Missing info", Toast.LENGTH_LONG).show();
            }

        }

        new AsyncGetUserInfo().execute(userID);
        Toast.makeText(AddItemActivity.this, "Job Added", Toast.LENGTH_LONG).show();

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

            }
        }
    }
    public void startProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("user_type", user_type);
        this.finish();
        //startActivity(intent);

    }

    public static class CustomItemFragment extends DialogFragment {
        CustomItemFragment newInstance(String title) {
            CustomItemFragment fragment = new CustomItemFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.view_custom_item, null);

            final EditText id = (EditText) view.findViewById(R.id.nameText);
            final EditText description = (EditText) view.findViewById(R.id.descriptionText);
            final EditText price = (EditText) view.findViewById(R.id.priceText);
            final EditText quant = (EditText) view.findViewById(R.id.quantityText);

            //connect to the edit_quantity to get original amount


            Button b = (Button) view.findViewById(R.id.addItemBtn);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                }
            });
            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle("Enter Item Name:")
                    .setView(view)
                    .setPositiveButton("Add",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View view = inflater.inflate(R.layout.view_custom_item, null);
                                    //connect to the edit_quantity to get original amount



                                    listItems.add(id.getText().toString());
                                    listDescription.add(description.getText().toString());
                                    listPrices.add(Double.parseDouble(price.getText().toString()));
                                    listQuantity.add(Integer.parseInt(quant.getText().toString()));
                                    adapter.notifyDataSetChanged();

                                    double tempPrice = 0.0;
                                    for(int i = 0; i < listPrices.size(); i++){
                                        tempPrice += Math.round((listPrices.get(i)*listQuantity.get(i))*100)/100.00d;
                                    }

                                    priceView.setText(tempPrice+"");
                                    // make call to get item price from db

                                    setListViewHeightBasedOnChildren(listView);


                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                }
                            }).create();
        }
    }
    public void addCustomProduct(View view){

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
                /*productID = product.getProduct_id();
                name = (TextView) findViewById(R.id.nameText);
                description = (TextView) findViewById(R.id.descriptionText);
                price = (TextView) findViewById(R.id.priceText);
                name.setText(product.getProduct_name());
                description.setText(product.getProduct_description());
                price.setText(String.format("%.2f", product.getAvg_price()));*/




                TextView p = (TextView) findViewById(R.id.totalPrice);
                //quantity = (TextView) findViewById(R.id.quantityText);
               // itemName = product.getProduct_name();
                listItems.add(product.getProduct_name());
                listPrices.add(product.getAvg_price());
                listDescription.add(product.getProduct_description());
                listQuantity.add(1);
                totalPrice += (product.getAvg_price());
                // make call to get item price from db

                p.setText(String.format("%.2f", totalPrice));
                setListViewHeightBasedOnChildren(listView);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        alertBuilder.show();
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
                    Toast.makeText(AddItemActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                String[] result2 = new String[10];
                result2 = result.split(",");
                Intent intent = new Intent(AddItemActivity.this, ProfileActivity.class);
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
                AddItemActivity.this.finish();
            }
        }
    }

}

