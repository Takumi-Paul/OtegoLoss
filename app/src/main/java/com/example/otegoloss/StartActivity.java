package com.example.otegoloss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button newAccountButton = (Button) findViewById(R.id.newAccountButton);
        newAccountButton.setOnClickListener(this);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newAccountButton:
                Intent accountIntent = new Intent(this, AccountRuleActivity.class);
                startActivity(accountIntent);
                break;
            case R.id.loginButton:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }
}