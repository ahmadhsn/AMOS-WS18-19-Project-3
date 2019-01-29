package com.gr03.amos.bikerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.NetworkLayer.SocketUtility;

public class SignUpActivity extends AppCompatActivity implements ResponseHandler {

    //EditText businessName;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //businessName = findViewById(R.id.business_name);
        email = findViewById(R.id.email);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rider:
                if (checked)
                   // businessName.setVisibility(View.GONE);
                break;
            case R.id.business_user:
                if (checked)
                    //businessName.setVisibility(VISIBLE);
                break;
        }
    }

    public void userSignUp(View view) throws JSONException{

        EditText password = findViewById(R.id.password);
        EditText confirm_pw = findViewById(R.id.confirm_password);
        //EditText businessName = findViewById(R.id.business_name);

        String pw = password.getText().toString();

        if (!pw.equals(confirm_pw.getText().toString()) || pw.isEmpty() || confirm_pw.getText().toString().isEmpty())   {
            Log.i("COMPAREPASSWORDS", "passwords are unequal");
            // this.setMessageOnScreen( "The passwords you entered do not match. Please try it again.",Color.RED);
            Toast.makeText(getApplicationContext(), "The passwords you entered do not match. Please try it again.", Toast.LENGTH_LONG).show();
            password.setText("");
            password.setError("");
            confirm_pw.setText("");
            confirm_pw.setError("");
            return;
        }

        String mail = email.getText().toString();

        //check if email address is valid
        if(!isValidEmail(mail)){
            Log.i("VALIDATIONMAIL", "mail address is not valid");
            email.setError("Invalid E-Mail address. Please check again");
            return;
        }


        RadioButton btBusinessUser = findViewById(R.id.business_user);
        Boolean businessUser = btBusinessUser.isChecked();

        JSONObject json = new JSONObject();
        json.put("email", email.getText().toString());
        json.put("password", pw);
        json.put("isBusinessUser", businessUser);
        //if (businessUser)
          //  json.put("business_name", businessName.getText().toString());

        Requests.executeRequest(this, "POST", "userRegistration", json);
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

        try {
            //handle response
            if(response.has("userRegistration")){
                String statusReg = (String) response.get("userRegistration");
                if(statusReg.equals("invalidMail")){
                    Log.i("VALIDATIONMAILSERVER", "invalid email address " + email.getText().toString());
                    Toast.makeText(getApplicationContext(), "Invalid email address. Please check again.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(statusReg.equals("successfullRegistration")){
                    Log.i("Registration","successfullRegistration");
                    Toast.makeText(getApplicationContext(), "You have successfully registered! You can login now.", Toast.LENGTH_LONG).show();
                    finish();

                }

                if(statusReg.equals("emailExistsAlready")){
                    Log.i("mailExists", "email address already exists");
                    Toast.makeText(getApplicationContext(), "A user with this email address is already registered. Try to login.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.i("Exception --- not requested", e.toString());
        }

    }
}