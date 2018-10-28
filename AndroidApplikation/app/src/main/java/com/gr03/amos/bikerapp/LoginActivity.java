package com.gr03.amos.bikerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Collects user name and password.
     * TODO: make req and it information
     * TODO: pw needs to be hashed
     * Caused by button click.
     * TODO: switches to the new activity
     *
     * @param view
     * @r
     */
    public void loginClick(android.view.View view) {
        EditText username = findViewById(R.id.username_input);
        EditText password = findViewById(R.id.password_input);
        Log.i("LoginData", "name: " + username.getText() + ", pw: " + password.getText());
    }
}