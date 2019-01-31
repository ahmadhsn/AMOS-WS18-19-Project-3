package com.gr03.amos.bikerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity implements ResponseHandler {
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
        if (!isValidEmail(email)) {
            Log.i("VALIDATIONMAIL", "mail address is not valid");
            emailId.setError("Invalid E-Mail address. Please check again");
            return;
        } else
            createAlertView();
        Requests.executeRequest(this, "PUT", "resetPassword", generateRequestJSON(email));
    }

    private void createAlertView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setMessage("We sent you a new password. Please check your Email-box!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        // Create the AlertDialog object and show it
        builder.create().show();
    }

    private JSONObject generateRequestJSON(String email) {
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("reset_email", email);
        } catch (JSONException e) {
        }
        return requestJSON;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onResponse(JSONObject response, String urlTail) {
        if (SocketUtility.hasSocketError(response)) {
            Toast.makeText(this, "No response from server.", Toast.LENGTH_LONG).show();
            return;
        }

    }
}
