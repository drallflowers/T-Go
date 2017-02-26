package com.may1722.t_go.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.may1722.t_go.R;

import org.w3c.dom.Text;

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

public class ProfileActivity extends AppCompatActivity {

    static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userID = getIntent().getExtras().getString("userID");
        String username = getIntent().getExtras().getString("username");
        String user_type = getIntent().getExtras().getString("user_type");
        String email = getIntent().getExtras().getString("email");
        String phone_number =  getIntent().getExtras().getString("phone_number");
        TextView profiletextView = (TextView) findViewById(R.id.profileUsername);
        TextView typetextView = (TextView) findViewById(R.id.profileUserLevel);
        TextView emailtextView = (TextView) findViewById(R.id.profileEmail);
        TextView phonetextView = (TextView) findViewById(R.id.profilePhoneLabel);
        profiletextView.setText(username);
        typetextView.setText(user_type);
        emailtextView.setText(email);
        phonetextView.setText(phone_number);


    }

    public void goToCurrentJobs(View view) {
        Intent intent = new Intent(this, CurrentJobs.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void goToJobSubmit(View view) {
        Intent intent = new Intent(this, JobSubmitActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void goToReviewBoard(View view) {
        Intent intent = new Intent(this, ReviewBoardActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void goToJobBoard(View view) {
        Intent intent = new Intent(this, JobBoardActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void goToMaps(View view)
    {
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
}
