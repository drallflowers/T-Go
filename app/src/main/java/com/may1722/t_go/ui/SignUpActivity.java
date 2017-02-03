package com.may1722.t_go.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.may1722.t_go.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = (EditText) findViewById(R.id.UserNameEditText);
       password = (EditText) findViewById(R.id.PasswordEditText);
       confirm_password = (EditText) findViewById(R.id.ConfirmPasswordEditText);
        setContentView(R.layout.activity_signup);
    }

    /*
     * On cancel, head back to login without touching database
     */
    public void goToLogin(View view) {

        //Sets up the class of the Activity we want to go to
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    /*
    * On submit, send information to DB  after checking fields and change screen to login
    */
    public void signUp(View view) {
        //Database insert & head to login
        TextView userField = (TextView) findViewById(R.id.UserNameEditText);
        TextView passField = (TextView) findViewById(R.id.PasswordEditText);
        TextView confirmPassField = (TextView) findViewById(R.id.ConfirmPasswordEditText);

        String username = userField.getText().toString();
        String password = passField.getText().toString();
        String confirmPassword = confirmPassField.getText().toString();
        if(!password.equals(confirmPassword)){
            //error message
        }
        else {
            if (attemptSignUp(username, password)) {
                Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
                startActivity(intent);
            } else {
                //display error message
            }
        }

    }

    private boolean attemptSignUp(String user, String pass){


         return true;
    }


    /**
     * Validates Text fields and updates database
     */
    protected void validateFields() {


    }

}
