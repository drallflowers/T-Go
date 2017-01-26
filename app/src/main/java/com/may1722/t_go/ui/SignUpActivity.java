package com.may1722.t_go.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.may1722.t_go.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    /*
     * On cancel, head back to login without touching database
     */
    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    /*
    * On submit, send information to DB and change screen to login
    */
    public void signUp(View view) {
        //Database insert & head to login

        goToLogin(view);
    }

}
