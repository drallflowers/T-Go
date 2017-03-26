package com.may1722.t_go.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.may1722.t_go.R;

public class JobCompleteRequesterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_complete_requester);

        String code = getIntent().getExtras().getString("verification_code");
        TextView codeText = (TextView) findViewById(R.id.codeText);
        codeText.setText("Verification Code: \n"+code);
    }
}
