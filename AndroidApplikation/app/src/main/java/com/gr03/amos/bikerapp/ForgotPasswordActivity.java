package com.gr03.amos.bikerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static EditText emailId;

    public ForgotPasswordActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        initViews();
    }

    // Initialize the views
    private void initViews() {
        emailId = findViewById(R.id.registered_emailid);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void submit(View view) throws JSONException {
        submitButtonTask();
    }

    private void submitButtonTask() {
        String email = emailId.getText().toString();
        if(!isValidEmail(email)){
            Log.i("VALIDATIONMAIL", "mail address is not valid");
            emailId.setError("Invalid E-Mail address. Please check again");
            return;
        }
        else
            try {
                FutureTask<String> task = new FutureTask(new Callable<String>() {
                    public String call() {
                        JSONObject threadResponse = Requests.getResponse("resetPassword", generateRequestJSON(email));
                        return threadResponse.toString();
                    }
                });

                new Thread(task).start();
                Log.i("Response", task.get());
                finish();
            } catch (Exception e) {
                //TODO: ErrorHandling
                Log.i("Exception --- not requested", e.toString());
            }
    }


    private JSONObject generateRequestJSON(String email) {
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("reset_email", email);
        } catch (JSONException e) {}
        return requestJSON;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
