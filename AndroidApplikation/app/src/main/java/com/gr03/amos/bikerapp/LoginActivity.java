package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    public void login(View view) throws JSONException {
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        if (email.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter your E-Mail address.", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter your password.", Toast.LENGTH_LONG).show();
            return;
        }

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
            //handle response
            if (response.has("success")) {

                String eml = response.getString("email");
                int userId = response.getInt("id_user");
                int userType = response.getInt("id_user_type");
                SaveSharedPreference.saveUserInforamtion(this, eml, userId, userType);
                Toast.makeText(getApplicationContext(), "You are logged in now!", Toast.LENGTH_LONG).show();


                Realm.init(this);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.createOrUpdateObjectFromJson(User.class, response);
                realm.commitTransaction();
                realm.close();

                User user = realm.where(User.class).findFirst();

                if (user.getId_user_type() == 2) {
                    Intent intent = new Intent(this, BusinessUserMainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, ShowEventActivity.class);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Wrong E-Mail address or password.", Toast.LENGTH_LONG).show();
            Log.i("Exception --- not requested", e.toString());
            return;
        }
    }
}