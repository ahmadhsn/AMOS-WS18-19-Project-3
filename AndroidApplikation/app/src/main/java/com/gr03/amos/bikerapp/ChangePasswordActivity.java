package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ChangePasswordActivity extends AppCompatActivity {
    Button changePassword;
    EditText email;
    EditText oldPassword;
    EditText newPassword;
    EditText repeatNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        changePassword = findViewById(R.id.change_password_button);
        email = findViewById(R.id.email);
        oldPassword = findViewById(R.id.editTextOldPassword);
        newPassword = findViewById(R.id.editTextNewPassword);
        repeatNewPassword = findViewById(R.id.editTextRepeatPassword);


        changePassword.setOnClickListener(v -> {
            try {
                changePassword();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void changePassword() throws JSONException {
        String np = newPassword.getText().toString();
        String rnp = repeatNewPassword.getText().toString();
        JSONObject json = new JSONObject();
        json.put("email", email.getText().toString());
        json.put("oldPassword", oldPassword.getText().toString());
        json.put("newPassword", newPassword.getText().toString());
        json.put("repeatNewPassword", repeatNewPassword.getText().toString());
        if (np.equals(rnp)) {
            try {

                JSONObject response;

                FutureTask<String> task = new FutureTask(new Callable<String>() {
                    public String call() {
                        JSONObject threadResponse = Requests.getResponse("changePassword", json);
                        return threadResponse.toString();
                    }
                });
                new Thread(task).start();
                Log.i("Response", task.get());
                response = new JSONObject(task.get());

                //handle response
                if (response.has("passwordUpdated")) {
                    String loginResponse = response.getString("passwordUpdated");

                    if (loginResponse.equals("successfullUpdation")) {
                        Toast.makeText(getApplicationContext(), "Password Successfully Changed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, ShowEventActivity.class);
                        startActivity(intent);
//                    return;
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Email or Old Password", Toast.LENGTH_LONG).show();
                        return;
                    }

//                if(loginResponse.equals("successfulLogin")){
//                    Toast.makeText(getApplicationContext(), "You are logged in now!", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(this, ShowEventActivity.class);
//                    startActivity(intent);
//
//                    //TODO create session and redirect to home screen
//                }
                }

            } catch (Exception e) {

                Log.i("Exception --- not requested", e.toString());
            }
        } else {
            Toast.makeText(getApplicationContext(), "New Password does not match", Toast.LENGTH_LONG).show();
        }

    }
}
