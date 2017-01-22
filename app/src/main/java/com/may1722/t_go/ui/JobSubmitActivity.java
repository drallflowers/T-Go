package com.may1722.t_go.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.*;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.may1722.t_go.R;

import java.util.Calendar;

public class JobSubmitActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private TextView jobDate;
    private TextView jobTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_submit);
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
        }
    }

    public void pickDate(View view){

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
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
}

