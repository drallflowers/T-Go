package com.may1722.t_go.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
//        Intent intent = new Intent(this, ProfileActivity.class);
//        startActivity(intent);
    }
    /*
    * On submit, send information to DB and change screen to login
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

}
