package com.gr03.amos.bikerapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.gr03.amos.bikerapp.Requests;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static android.view.View.VISIBLE;

public class SignUpActivity extends AppCompatActivity {

    EditText businessName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        businessName = findViewById(R.id.business_name);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rider:
                if (checked)
                    businessName.setVisibility(View.GONE);
                break;
            case R.id.business_user:
                if (checked)
                    businessName.setVisibility(VISIBLE);
                break;
        }
    }

    public void userSignUp(View view) throws JSONException{
        this.setMessageOnScreen("");

        EditText password = findViewById(R.id.password);
        EditText confirm_pw = findViewById(R.id.confirm_password);

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

        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        String mail = email.getText().toString();

        //check if email address is valid
        if(!isValidEmail(mail)){
            Log.i("VALIDATIONMAIL", "mail address is not valid");
            email.setError("Invalid E-Mail address. Please check again");
            return;
        }


        Boolean businessUser = false;
        if (businessName.VISIBLE == 0) businessUser = true;

        JSONObject json = new JSONObject();
        json.put("name", name.getText().toString());
        json.put("email", email.getText().toString());
        json.put("password", pw);
        json.put("isBusinessUser", businessUser.booleanValue());
        if (businessUser) json.put("business_name", businessName.getText().toString());

        try {
            JSONObject response;

            FutureTask<String> task = new FutureTask(new Callable<String>() {
                public String call() {
                    JSONObject threadResponse = Requests.getResponse("userRegistration", json);
                    return threadResponse.toString();
                }
            });
            new Thread(task).start();
            Log.i("Response", task.get());
            response = new JSONObject(task.get());

            //handle response
            if(response.has("userRegistration")){
                String statusReg = (String) response.get("userRegistration");
                this.setMessageOnScreen(statusReg);
                if(statusReg.equals("invalidMail")){
                    Log.i("VALIDATIONMAILSERVER", "invalid email address " + email.getText().toString());
                    Toast.makeText(getApplicationContext(), "Invalid email address. Please check again.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(statusReg.equals("successfullRegistration")){
                    Log.i("Registration","successfullRegistration");
                    Toast.makeText(getApplicationContext(), "You have successfully registered! You can login now.", Toast.LENGTH_LONG).show();
                    password.setText("");
                    confirm_pw.setText("");
                    name.setText("");
                    email.setText("");

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

    /**
     * Sets a message on the screen in the text field "message"
     *
     * @param message Message to show the user
     */
    public void setMessageOnScreen(String message){
        TextView textError = (TextView) findViewById(R.id.message);

        textError.setText(message);
    }

    /**
     * Sets a message on the screen in a specific color in the text field "message"
     *
     * @param message Message to show the user
     * @param color Color of the text
     */
    public void setMessageOnScreen(String message, int color){
        setMessageOnScreen(message);
        TextView textError = (TextView) findViewById(R.id.message);
        textError.setTextColor(color);

    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}