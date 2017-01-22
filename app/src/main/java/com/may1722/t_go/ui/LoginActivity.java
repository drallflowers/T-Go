package com.may1722.t_go.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.may1722.t_go.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToSignup(View view){
        //Intent intent = new Intent(this, SignUpActivity.class);   //Sets up the class of the Activity we want to go to
        //startActivity(intent);                                    //Starts teh Activity we want to go to
    }



    public void login(View view){

        Intent intent = new Intent(this, JobSubmitActivity.class);   //Sets up the class of the Activity we want to go to
        startActivity(intent);
        //Will get setup to send text in UserNameEditText and PasswordEditText to database to check if the user exists
    }
}
