package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) throws JSONException {
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        JSONObject json = new JSONObject();
        json.put("email", email.getText().toString());
        json.put("password", password.getText().toString());

        try {
            JSONObject response;

            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("checkUser", json);
                    return threadResponse.toString();
                }
            });
            new Thread(task).start();
            Log.i("Response", task.get());
            response = new JSONObject(task.get());
            Log.i("this is response", String.valueOf(response));
            //handle response
            if (response.has("success")) {

                Log.i("IGI", String.valueOf(response));
                String eml = response.getString("email");
                int userId = response.getInt("user_id");
                SaveSharedPreference.saveUserInforamtion(this, eml, userId);
                Toast.makeText(getApplicationContext(), "You are logged in now!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, ShowEventActivity.class);
                startActivity(intent);

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
            Log.i("Exception --- not requested", e.toString());
            return;
        }
    }
}