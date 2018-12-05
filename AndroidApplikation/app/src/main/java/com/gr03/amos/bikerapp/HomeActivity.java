package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    Button loginButton;
    Button signUpButton;
    Button createEventButton;
    Button EventTypeButton;
    Button ChangePasswordButton;
    Button AddProfile;
    Button createRouteButton;
    Button findUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();

        signUpButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);
        ChangePasswordButton = findViewById(R.id.change_password_button);
        AddProfile = findViewById(R.id.AddProfile_Button);
        findUser = findViewById(R.id.finduser_button);

        createRouteButton = findViewById(R.id.createRouteButton);
        
        createRouteButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddRoute.class);
            startActivity(intent);
        });
        
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        AddProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddProfileBasicUserActivity.class);
            startActivity(intent);
        });
        findUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowFriendsActivity.class);
            startActivity(intent);
        });

    }
}

