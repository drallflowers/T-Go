package com.may1722.t_go.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.may1722.t_go.R;
import com.may1722.t_go.model.ListViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class JobSubmitActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private TextView jobDate;
    private TextView jobTime;
    private TextView description;
    private TextView address;
    static TextView price;
    private TextView title;
    static String itemName;
    static ListView listView;
    static ArrayList<String> listItems;
    static ArrayList<Double> listPrices;
    static ListViewAdapter adapter;
    static double totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }


        setContentView(R.layout.activity_job_submit);
        title = (TextView) findViewById(R.id.editTitle);
        price = (TextView) findViewById(R.id.totalPrice);
        description = (TextView) findViewById(R.id.editDescription);
        address = (TextView) findViewById(R.id.addressText);
        totalPrice = 0.00;
        listView = (ListView) findViewById(R.id.listView);
        listItems = new ArrayList<>();
        listPrices = new ArrayList<>();
        adapter = new ListViewAdapter(listItems,listPrices, this);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        itemName = "";
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
    public void onDateSet(DatePicker view, int year, int month, int day){
    jobDate = (TextView) findViewById(R.id.dateView);
        jobDate.setText(new StringBuilder().append(month+1).append("-").append(day).append("-").append(year));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        jobDate = (TextView) findViewById(R.id.timeView);
        if(hourOfDay > 12){
            jobDate.setText(new StringBuilder().append(hourOfDay%12).append(":").append(minute).append(" PM")); // show PM if hour day > 12
        }
        else {
            jobDate.setText(new StringBuilder().append(hourOfDay).append(":").append(minute).append(" AM")); // show AM
        } // show PM if hour day > 12
    }

    public void pickDate(View view){

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void submit(View view) throws JSONException {
        JSONObject obj = new JSONObject();

        //might want to take this out and make it into a method. not sure though since it is kind of Job specific.

        //Check data is valid, then add job to database, then go to CurrentJobs -JRS
        // Make job JSON object after it is validated

        // NOTE NEED TO CHANGE NAME WITH USER
        obj.put("Owner", "Name");
        obj.put("Title",title.getEditableText().toString());
        obj.put("Description", description.getEditableText().toString());
        obj.put("Price", price.getEditableText().toString());
        obj.put("Address", address.getEditableText().toString());
        obj.put("Date", jobDate.getEditableText().toString());
        obj.put("Time", jobTime.getEditableText().toString());
        Intent intent = new Intent(this, CurrentJobs.class);
        startActivity(intent);
    }

        //Will get setup to send text in UserNameEditText and PasswordEditText to database to check if the user exists

    public static class DatePickerFragment extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
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
                    p.setText("1.00");

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


    }

public static class TimePickerFragment extends DialogFragment  {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),(JobSubmitActivity) getActivity() , hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

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

