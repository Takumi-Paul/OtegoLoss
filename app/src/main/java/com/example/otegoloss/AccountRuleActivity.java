package com.example.otegoloss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountRuleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        Button newAccountButton = (Button) findViewById(R.id.agreeButton);
        newAccountButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent accountIntent = new Intent(this, NewAccountActivity.class);
        startActivity(accountIntent);
    }
}