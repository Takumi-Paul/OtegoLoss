package com.example.otegoloss;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.otegoloss.user.UserFragment;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences userIDData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userIDData = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        String userID = userIDData.getString("userID", "");
        System.out.println(userID);
        if (userID != "") {
            // HOME画面に遷移
            Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(mainIntent);
        } else {
            setTheme(R.style.Theme_OtegoLoss);
            setContentView(R.layout.activity_start);

            Button newAccountButton = (Button) findViewById(R.id.newAccountButton);
            newAccountButton.setOnClickListener(this);
            Button loginButton = (Button) findViewById(R.id.loginButton);
            loginButton.setOnClickListener(this);
        }
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