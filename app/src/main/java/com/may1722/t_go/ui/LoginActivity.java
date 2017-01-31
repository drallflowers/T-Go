package com.may1722.t_go.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.may1722.t_go.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToSignup(View view){
        Intent intent = new Intent(this, SignUpActivity.class);   //Sets up the class of the Activity we want to go to
        startActivity(intent);                                    //Starts the Activity we want to go to
    }



    public void login(View view){
        TextView userField = (TextView) findViewById(R.id.UserNameEditText);
        TextView passField = (TextView) findViewById(R.id.PasswordEditText);

        String username = userField.getText().toString();
        String password = passField.getText().toString();

        if(attemptLogin(username,password)){
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else{
            //display error message
        }

    }

    private boolean attemptLogin(String user, String pass){
        //send request to php server
        return true;
    }
}
