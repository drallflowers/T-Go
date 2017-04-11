package com.may1722.t_go.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.may1722.t_go.R;



public class ProfileActivity extends AppCompatActivity {

    static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;
    private String userID;
    private String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userID = getIntent().getExtras().getString("userID");
        String username = getIntent().getExtras().getString("username");
        user_type = getIntent().getExtras().getString("user_type");
        String email = getIntent().getExtras().getString("email");
        String phone_number =  getIntent().getExtras().getString("phone_number");
        TextView profiletextView = (TextView) findViewById(R.id.profileUsername);
        TextView typetextView = (TextView) findViewById(R.id.profileUserLevel);
        TextView emailtextView = (TextView) findViewById(R.id.profileEmail);
        TextView phonetextView = (TextView) findViewById(R.id.profilePhoneLabel);
        profiletextView.setText(username);

        if(user_type.equals("1")){
            typetextView.setText("Courier");
        }else if(user_type.equals("2")){
            typetextView.setText("Moderator");
        }else{
            typetextView.setText("User");
        }

        emailtextView.setText(email);
        phonetextView.setText(phone_number);

        Button findJob = (Button) findViewById(R.id.buttonFindJobs);
        Button myReviews = (Button) findViewById(R.id.buttonReviews);


        if(Integer.parseInt(user_type) == 0){
            findJob.setVisibility(View.INVISIBLE);
            myReviews.setVisibility(View.INVISIBLE);
            findJob.setEnabled(false);
            myReviews.setEnabled(false);
        }



    }

    public void updateUserInfo(View view) {
        Intent intent = new Intent(this, UpdateUserInfoActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void goToCurrentJobs(View view) {
        Intent intent = new Intent(this, CurrentJobs.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void goToUpgradeAccount(View view) {
        Intent intent = new Intent(this, ApplyActivity.class);
        intent.putExtra("type", user_type);
        intent.putExtra("userID", userID);

        startActivity(intent);
    }

    public void goToMailBoard(View view) {
        Intent intent = new Intent(this, MailActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void goToJobSubmit(View view) {
        Intent intent = new Intent(this, JobSubmitActivity.class);
        intent.putExtra("type", user_type);
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
