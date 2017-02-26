package com.may1722.t_go.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateTimePatternGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.*;
import android.support.v7.app.ActionBar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.may1722.t_go.R;
import com.may1722.t_go.model.ListViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import java.util.Calendar;
import java.util.Random;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class JobSubmitActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView jobDate;
    private TextView jobTime;
    private TextView description;
    private TextView address;
    static TextView price;
    private TextView title;
    private TextView zip;
    private TextView state;
    private TextView apartment;
    private TextView city;
    private String userID;
    private Integer locationID;
    private String dateString;

    static String itemName;
    static ListView listView;
    static ArrayList<String> listItems;
    static ArrayList<Double> listPrices;
    static ListViewAdapter adapter;
    static double totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set view to the activity
        setContentView(R.layout.activity_job_submit);

        userID = getIntent().getExtras().getString("userID");

        // link to views in the activity
        title = (TextView) findViewById(R.id.editTitle);
        price = (TextView) findViewById(R.id.totalPrice);
        description = (TextView) findViewById(R.id.editDescription);
        address = (TextView) findViewById(R.id.addressText);
        listView = (ListView) findViewById(R.id.listView);

        // initialize objects
        totalPrice = 0.00;
        listItems = new ArrayList<>();
        listPrices = new ArrayList<>();
        adapter = new ListViewAdapter(listItems,listPrices, this);
        listView.setAdapter(adapter);
        itemName = "";


        setListViewHeightBasedOnChildren(listView);
        Button addItemBtn = (Button) findViewById(R.id.addItemBtn);

        addItemBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                totalPrice = Double.parseDouble(price.getText().toString());
                DialogFragment newFragment = new ItemLookUpFragment();
                newFragment.show(getSupportFragmentManager(), "itemSelect");

            }
        });



    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        jobDate = (TextView) findViewById(R.id.dateView);
        jobDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        jobTime = (TextView) findViewById(R.id.timeView);
        /*if (hourOfDay > 12) {
            jobTime.setText(new StringBuilder().append(hourOfDay % 12).append(":").append(minute).append(" PM")); // show PM if hour day > 12
        } else {*/
            jobTime.setText(new StringBuilder().append(hourOfDay).append(":").append(minute)); // show AM
        //}
    }

    public void pickDate(View view) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void submit(View view) throws JSONException {
        JSONObject obj = new JSONObject();
        //title = (TextView) findViewById(R.id.editTitle);
        //price = (TextView) findViewById(R.id.editPrice);
        //description = (TextView) findViewById(R.id.editDescription);
        address = (TextView) findViewById(R.id.addressText);
        zip = (TextView) findViewById(R.id.zipText);
        city = (TextView) findViewById(R.id.cityText);
        apartment = (TextView) findViewById(R.id.apartmentText);
        state = (TextView) findViewById(R.id.stateText);

        //might want to take this out and make it into a method. not sure though since it is kind of Job specific.

        //Check data is valid, then add job to database, then go to CurrentJobs -JRS
        // Make job JSON object after it is validated

        // NOTE NEED TO CHANGE NAME WITH USER
        /*obj.put("Owner", "Name");
        obj.put("Title",title.getEditableText().toString());
        obj.put("Description", description.getEditableText().toString());
        obj.put("Price", price.getEditableText().toString());
        obj.put("Address", address.getEditableText().toString());
        obj.put("Date", jobDate.getEditableText().toString());
        obj.put("Time", jobTime.getEditableText().toString());*/

        dateString = jobDate.getText().toString() + " " + jobTime.getText().toString() + ":00";
        String streetString = address.getEditableText().toString();
        String cityString = city.getEditableText().toString();
        String stateString = state.getEditableText().toString();
        String zipString = zip.getEditableText().toString();
        String apartmentString = apartment.getEditableText().toString();
        //String descriptionString = description.getEditableText().toString();


        if (streetString.length() > 0 && cityString.length() > 0 && stateString.length() > 0 && zipString.length() > 0) {
            new AsyncAddLocation().execute(streetString, cityString, stateString, zipString, apartmentString);
            // START NEW TASK HERE

        } else {
            Toast.makeText(JobSubmitActivity.this, "Missing info", Toast.LENGTH_LONG).show();
        }
    }

//Will get setup to send text in UserNameEditText and PasswordEditText to database to check if the user exists

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),R.style.style_date_picker_dialog, (JobSubmitActivity)getActivity(), year, month, day);
        }
    }

    public static class ItemLookUpFragment extends DialogFragment {
        static ItemLookUpFragment newInstance(String title) {
            ItemLookUpFragment fragment = new ItemLookUpFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.view_add_item, null);
            final EditText t = (EditText)view.findViewById(R.id.item_search);
            final TextView p = (TextView)view.findViewById(R.id.item_est_price);
            Button b = (Button)view.findViewById(R.id.search_btn);
            b.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    Random r = new Random();
                    double randomValue = 1 + (10 - 1) * r.nextDouble();
                    DecimalFormat decim = new DecimalFormat("0.00");
                    String s = decim.format(randomValue);
                    p.setText(s);

                }
            });
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Enter Item Name:")
                    .setView(view)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    itemName =  t.getText().toString();
                                    Double tempPrice = Double.parseDouble(p.getText().toString());
                                    listItems.add(itemName);
                                    listPrices.add(tempPrice);
                                    DecimalFormat decim = new DecimalFormat("0.00");
                                    totalPrice+=tempPrice;
                                    String s = decim.format(totalPrice);
                                    price.setText(s);
                                    // make call to get item price from db
                                    setListViewHeightBasedOnChildren(listView);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                }
                            }).create();
        }

public static class TimePickerFragment extends DialogFragment  {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (JobSubmitActivity) getActivity(), hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

    }

    private class AsyncAddLocation extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/addlocation.inc.php");

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
                        .appendQueryParameter("street_address", params[0])
                        .appendQueryParameter("city", params[1])
                        .appendQueryParameter("state", params[2])
                        .appendQueryParameter("zipcode", params[3])
                        .appendQueryParameter("apt_number", params[4]);
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
                Toast.makeText(JobSubmitActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobSubmitActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                locationID = Integer.parseInt(result);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                new AsyncAddJob().execute(userID, formattedDate, dateString, locationID.toString());
                //Toast.makeText(JobSubmitActivity.this, "Getting there", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AsyncAddJob extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://may1722db.ece.iastate.edu/addjob.inc.php");

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
                        .appendQueryParameter("date_posted", params[1])
                        .appendQueryParameter("date_due", params[2])
                        .appendQueryParameter("delivery_location_id", params[3]);
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
                Toast.makeText(JobSubmitActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(JobSubmitActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            } else {
                startAddItemActivity(result);
                //Toast.makeText(JobSubmitActivity.this, "Getting there", Toast.LENGTH_LONG).show();

            }
        }


    }

    public void startAddItemActivity(String jobID){
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("jobID", jobID);
        startActivity(intent);
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


}

